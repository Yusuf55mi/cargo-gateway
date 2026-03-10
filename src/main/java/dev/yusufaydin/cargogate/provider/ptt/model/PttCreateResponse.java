package dev.yusufaydin.cargogate.provider.ptt.model;

import lombok.Getter;
import lombok.Setter;

/**
 * ptt-cargo-api GeneralResponse<KabulEkleResponse> sablonu:
 * { "status": 200, "error": null, "data": { "url": "...", "barcode": 123456 } }
 */
@Getter
@Setter
public class PttCreateResponse {
    private int status;
    private String error;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String url;
        private Long barcode;
    }
}
