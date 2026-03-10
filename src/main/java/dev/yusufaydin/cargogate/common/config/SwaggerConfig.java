package dev.yusufaydin.cargogate.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cargo Gateway API")
                        .description("PTT, Yurtici Kargo ve MNG icin unified kargo gateway")
                        .version("1.0.0")
                        .contact(new Contact().name("Yusuf Aydin")));
    }
}
