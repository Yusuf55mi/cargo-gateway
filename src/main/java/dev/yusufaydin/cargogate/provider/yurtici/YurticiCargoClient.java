package dev.yusufaydin.cargogate.provider.yurtici;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Yurtici SOAP endpoint'ine raw XML/text gonderir.
 * Freemarker template'leri SOAP zarfini uretir, bu client iletir.
 */
@FeignClient(name = "yurticiCargoClient", url = "${yurtici.base-url}")
public interface YurticiCargoClient {

    @PostMapping(consumes = "text/xml;charset=UTF-8", produces = "text/xml")
    String call(@RequestBody String soapXml);
}
