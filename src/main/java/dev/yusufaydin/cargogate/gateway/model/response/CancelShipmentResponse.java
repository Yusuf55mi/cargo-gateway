package dev.yusufaydin.cargogate.gateway.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelShipmentResponse {
    private String trackingNo;
    private String message;
}
