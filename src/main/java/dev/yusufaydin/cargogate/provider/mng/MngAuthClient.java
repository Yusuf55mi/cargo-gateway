package dev.yusufaydin.cargogate.provider.mng;

import dev.yusufaydin.cargogate.provider.mng.model.MngTokenRequest;
import dev.yusufaydin.cargogate.provider.mng.model.MngTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * MNG token endpoint'i.
 * POST {baseUrl}/mngapi/api/token
 * Header: x-ibm-client-id, x-ibm-client-secret
 */
@FeignClient(name = "mngAuthClient", url = "${mng.base-url}")
public interface MngAuthClient {

    @PostMapping(value = "/mngapi/api/token", consumes = "application/json", produces = "application/json")
    MngTokenResponse getToken(
            @RequestHeader("x-ibm-client-id") String clientId,
            @RequestHeader("x-ibm-client-secret") String clientSecret,
            @RequestBody MngTokenRequest request
    );
}
