package ren.infox.ddns.configure;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import ren.infox.ddns.service.DdnsScheduler;

public class NetworkHealthCheckIndicator extends AbstractHealthIndicator {

    private final DdnsScheduler ddnsScheduler;

    public NetworkHealthCheckIndicator(DdnsScheduler ddnsScheduler) {
        this.ddnsScheduler = ddnsScheduler;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            ddnsScheduler.updateDNS();
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }
    }
}
