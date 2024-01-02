package ren.infox.ddns.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class IpifyClient {

    private final IpifyService ipifyService;
    public IpifyClient(@Value("${ipify.host}") String baseUrl) {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).defaultHeader("user-agent", "Mozilla").build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        ipifyService = httpServiceProxyFactory.createClient(IpifyService.class);
    }

    public IpifyService getIpifyService() {
        return ipifyService;
    }
}
