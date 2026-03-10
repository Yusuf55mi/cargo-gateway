package dev.yusufaydin.cargogate.provider.mng;

import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderRequest;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderResponse;
import dev.yusufaydin.cargogate.provider.mng.model.MngTrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MNG kargo endpoint'leri.
 * Tum istekler: Authorization: Bearer {token}
 */
@FeignClient(name = "mngCargoClient", url = "${mng.base-url}")
public interface MngCargoClient {

    @PostMapping(value = "/mngapi/api/standardcmdapi/createOrder", consumes = "application/json", produces = "application/json")
    MngCreateOrderResponse createOrder(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("x-ibm-client-id") String clientId,
            @RequestHeader("x-ibm-client-secret") String clientSecret,
            @RequestBody MngCreateOrderRequest request
    );

    @PutMapping("/mngapi/api/standardcmdapi/cancelorder/{refrenceId}")
    void cancelOrder(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("x-ibm-client-id") String clientId,
            @RequestHeader("x-ibm-client-secret") String clientSecret,
            @PathVariable("refrenceId") String referenceId
    );

    @GetMapping("/mngapi/api/standardqueryapi/trackshipment/{referenceId}")
    List<MngTrackResponse> trackByReference(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("x-ibm-client-id") String clientId,
            @RequestHeader("x-ibm-client-secret") String clientSecret,
            @PathVariable("referenceId") String referenceId
    );
}
