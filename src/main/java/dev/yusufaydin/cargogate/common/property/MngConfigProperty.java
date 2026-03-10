package dev.yusufaydin.cargogate.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mng")
public class MngConfigProperty {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String customerNumber;
    private String password;
    private int tokenTtlMinutes = 480;
}
