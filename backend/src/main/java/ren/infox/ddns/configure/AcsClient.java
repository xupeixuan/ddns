package ren.infox.ddns.configure;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcsClient {
    @Value("${alidns.regionId}")
    private String regionId;
    @Value("${alidns.accessKeyId}")
    private String accessKeyId;
    @Value("${alidns.secret}")
    private String secret;
    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        HttpClientConfig httpClientConfig = HttpClientConfig.getDefault();
        httpClientConfig.setProtocolType(ProtocolType.HTTPS);
        profile.setHttpClientConfig(httpClientConfig);
        return new DefaultAcsClient(profile);
    }
}
