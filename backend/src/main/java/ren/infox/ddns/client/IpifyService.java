package ren.infox.ddns.client;

import org.springframework.web.service.annotation.GetExchange;

public interface IpifyService {
    @GetExchange("/jsonip")
    RemoteIpRecord getRemoteIp();
}
