package dev.yusufaydin.cargogate.gateway.controller;

import dev.yusufaydin.cargogate.common.model.GeneralResponse;
import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;
import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import dev.yusufaydin.cargogate.gateway.service.CargoGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cargo")
@RequiredArgsConstructor
@Tag(name = "Cargo Gateway", description = "PTT, Yurtici ve MNG icin unified endpoint")
public class CargoGatewayController {

    private final CargoGatewayService cargoGatewayService;

    @Operation(summary = "Gonderi olustur")
    @PostMapping("/create")
    public GeneralResponse<CreateShipmentResponse> createShipment(
            @Valid @RequestBody CreateShipmentRequest request) {
        return GeneralResponse.<CreateShipmentResponse>builder()
                .data(cargoGatewayService.createShipment(request))
                .build();
    }

    @Operation(summary = "Gonderi iptal et")
    @PostMapping("/cancel")
    public GeneralResponse<CancelShipmentResponse> cancelShipment(
            @Valid @RequestBody CancelShipmentRequest request) {
        return GeneralResponse.<CancelShipmentResponse>builder()
                .data(cargoGatewayService.cancelShipment(request))
                .build();
    }

    @Operation(summary = "Gonderi takibi")
    @GetMapping("/{trackingNo}/status")
    public GeneralResponse<TrackShipmentResponse> trackShipment(
            @PathVariable String trackingNo,
            @RequestParam CargoFirm firm) {
        return GeneralResponse.<TrackShipmentResponse>builder()
                .data(cargoGatewayService.trackShipment(firm, trackingNo))
                .build();
    }
}
