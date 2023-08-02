package ren.infox.ddns.configure;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ren.infox.ddns.service.DdnsScheduler;

@Configuration
public class NetworkConfiguration {

    @Bean
    @ConditionalOnEnabledHealthIndicator("network")
    public NetworkHealthCheckIndicator networkHealthContributor(DdnsScheduler ddnsScheduler) {
        return new NetworkHealthCheckIndicator(ddnsScheduler);
    }
}
