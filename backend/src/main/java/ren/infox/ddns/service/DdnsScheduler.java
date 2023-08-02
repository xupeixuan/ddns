package ren.infox.ddns.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ren.infox.ddns.client.IpifyClient;
import ren.infox.ddns.client.IpifyService;

import java.util.Arrays;
import java.util.List;

@Component
@SuppressWarnings("all")
public class DdnsScheduler {
    private static final Logger logger = LoggerFactory.getLogger(DdnsScheduler.class);

    @Value("${alidns.dns.domain}")
    private String domainName;
    @Value("#{'${alidns.dns.subDomains}'.split(',')}")
    private String[] subDomains;
    @Value("${alidns.dns.type}")
    private String domainType;

    private final IpifyService ipifyService;
    private final IAcsClient iAcsClient;

    public DdnsScheduler(IpifyClient ipifyClient, IAcsClient iAcsClient) {
        this.ipifyService = ipifyClient.getIpifyService();
        this.iAcsClient = iAcsClient;
    }

    public void updateDNS() {
        String currentClientIp = ipifyService.getRemoteIp().ip();
        if (StringUtils.isEmpty(currentClientIp)) {
            return;
        }

        Arrays.stream(subDomains).forEach(subDomain -> {
            List<DescribeDomainRecordsResponse.Record> ipRecords = fetchRemoteRecord(subDomain);
            ipRecords.stream().findFirst().ifPresentOrElse(r -> {
                if (!StringUtils.equalsIgnoreCase(r.getValue(), currentClientIp)) {
                    updateRemoteDomainRecord(currentClientIp, r, subDomain);
                }
            }, () -> addRemoteDomainRecord(currentClientIp, subDomain));
            logger.info("subDomain {}, the current ip {}, remote dns record {}", subDomain, currentClientIp, ipRecords.stream().map(DescribeDomainRecordsResponse.Record::getValue).toList());
        });
    }

    private void addRemoteDomainRecord(String ip, String subDomain) {
        AddDomainRecordRequest addDomainRecordRequest = new AddDomainRecordRequest();
        addDomainRecordRequest.setDomainName(domainName);
        addDomainRecordRequest.setRR(subDomain);
        addDomainRecordRequest.setType(domainType);
        addDomainRecordRequest.setValue(ip);
        try {
            AddDomainRecordResponse addDomainRecordResponse = iAcsClient.getAcsResponse(addDomainRecordRequest);
            logger.info("{}", addDomainRecordResponse);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateRemoteDomainRecord(String ip, DescribeDomainRecordsResponse.Record r, String subDomain) {
        UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
        updateDomainRecordRequest.setRR(subDomain);
        updateDomainRecordRequest.setType(domainType);
        updateDomainRecordRequest.setRecordId(r.getRecordId());
        updateDomainRecordRequest.setValue(ip);
        try {
            UpdateDomainRecordResponse updateDomainRecordResponse = iAcsClient.getAcsResponse(updateDomainRecordRequest);
            logger.info("Hit it!  update result: {}", updateDomainRecordResponse);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DescribeDomainRecordsResponse.Record> fetchRemoteRecord(String subDomain) {
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        describeDomainRecordsRequest.setDomainName(domainName);
        describeDomainRecordsRequest.setRRKeyWord(subDomain);
        describeDomainRecordsRequest.setType(domainType);
        DescribeDomainRecordsResponse describeDomainRecordsResponse;
        try {
            describeDomainRecordsResponse = iAcsClient.getAcsResponse(describeDomainRecordsRequest);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return describeDomainRecordsResponse.getDomainRecords();
    }
}
