package dev.yusufaydin.cargogate.gateway.model.request;

import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Gonderi olusturma istegi")
public class CreateShipmentRequest {

    @NotNull(message = "cargoFirm bos olamaz")
    @Schema(example = "PTT")
    private CargoFirm cargoFirm;

    @NotNull(message = "receiver bos olamaz")
    @Valid
    private ShipmentAddress receiver;

    @NotNull(message = "sender bos olamaz")
    @Valid
    private ShipmentAddress sender;

    @Schema(description = "Referans no. PTT from-client icin zorunlu; Yurtici/MNG icin opsiyonel.", example = "REF-2024-001")
    private String referenceNo;

    @Schema(description = "Desi. Yurtici icin zorunlu.", example = "5")
    private Integer desi;

    @Schema(description = "Kapida tahsilat tutari. 0 veya null = yok.", example = "150.00")
    private BigDecimal collectionAmount;

    @Schema(description = "Kapida odeme yontemi (collectionAmount > 0 ise gecerli)", example = "CASH")
    private CollectionType collectionType;

    public enum CollectionType {
        CASH, CREDIT_CARD
    }
}
