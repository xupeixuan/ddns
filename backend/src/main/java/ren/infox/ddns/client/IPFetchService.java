package ren.infox.ddns.client;

import org.springframework.web.service.annotation.GetExchange;

public interface IPFetchService {
    @GetExchange("/jsonip")
    RemoteIpRecord getRemoteIp();
}
