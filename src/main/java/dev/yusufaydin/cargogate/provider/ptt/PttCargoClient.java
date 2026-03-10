package dev.yusufaydin.cargogate.provider.ptt;

import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateFromClientRequest;
import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateFromCompanyRequest;
import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateResponse;
import dev.yusufaydin.cargogate.provider.ptt.model.PttTrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ptt-cargo-api'ye baglanan FeignClient.
 * ptt-cargo-api port 8080'de ayakta olmalidir.
 */
@FeignClient(name = "pttCargoClient", url = "${ptt-service.base-url}")
public interface PttCargoClient {

    @PostMapping("/api/v2/ptt/from-client")
    PttCreateResponse createFromClient(@RequestBody PttCreateFromClientRequest request);

    @PostMapping("/api/v2/ptt/from-company")
    PttCreateResponse createFromCompany(@RequestBody PttCreateFromCompanyRequest request);

    @GetMapping("/api/v2/ptt/ref/{refNo}")
    PttTrackResponse track(@PathVariable String refNo);
}
