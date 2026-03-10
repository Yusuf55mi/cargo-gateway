package dev.yusufaydin.cargogate.gateway.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Gonderen veya alici adres/kisi bilgileri")
public class ShipmentAddress {

    @NotBlank(message = "firstName bos olamaz")
    @Schema(example = "Ahmet")
    private String firstName;

    @NotBlank(message = "lastName bos olamaz")
    @Schema(example = "Yilmaz")
    private String lastName;

    @NotBlank(message = "phone bos olamaz")
    @Schema(example = "5551234567")
    private String phone;

    @NotBlank(message = "address bos olamaz")
    @Schema(example = "Ataturk Cad. No:1 D:3")
    private String address;

    @NotBlank(message = "city bos olamaz")
    @Schema(example = "Istanbul")
    private String city;

    @NotBlank(message = "district bos olamaz")
    @Schema(example = "Kadikoy")
    private String district;
}
