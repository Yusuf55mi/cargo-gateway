package dev.yusufaydin.cargogate.gateway.model.request;

import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Gonderi iptal istegi")
public class CancelShipmentRequest {

    @NotNull(message = "cargoFirm bos olamaz")
    @Schema(example = "YURTICI")
    private CargoFirm cargoFirm;

    @NotBlank(message = "trackingNo bos olamaz")
    @Schema(example = "1234567890123")
    private String trackingNo;
}
