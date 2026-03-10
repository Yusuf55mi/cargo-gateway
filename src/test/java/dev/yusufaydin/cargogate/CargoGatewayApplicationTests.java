package dev.yusufaydin.cargogate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "ptt-service.base-url=http://localhost:9999",
    "yurtici.base-url=http://localhost:9999",
    "mng.base-url=http://localhost:9999",
    "mng.client-id=test",
    "mng.client-secret=test",
    "mng.customer-number=test",
    "mng.password=test"
})
class CargoGatewayApplicationTests {

    @Test
    void contextLoads() {
    }
}
