package dev.yusufaydin.cargogate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CargoGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(CargoGatewayApplication.class, args);
    }
}
