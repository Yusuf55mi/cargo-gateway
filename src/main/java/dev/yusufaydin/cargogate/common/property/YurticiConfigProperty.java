package dev.yusufaydin.cargogate.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "yurtici")
public class YurticiConfigProperty {
    private String baseUrl;
    private String username;
    private String password;
    private String userLanguage = "TR";
}
