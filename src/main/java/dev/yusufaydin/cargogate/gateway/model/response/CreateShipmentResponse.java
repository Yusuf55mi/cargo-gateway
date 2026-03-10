package dev.yusufaydin.cargogate.gateway.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateShipmentResponse {
    private String trackingNo;
    private String barcode;
    private String labelUrl;
    private String referenceNo;
}
