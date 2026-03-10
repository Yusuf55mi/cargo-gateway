package dev.yusufaydin.cargogate.gateway.service;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;
import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import dev.yusufaydin.cargogate.gateway.provider.CargoProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargoGatewayService {

    private final List<CargoProvider> providers;
    private Map<CargoFirm, CargoProvider> providerMap;

    @PostConstruct
    public void init() {
        providerMap = new EnumMap<>(CargoFirm.class);
        for (CargoProvider provider : providers) {
            providerMap.put(provider.supports(), provider);
        }
        log.info("Aktif kargo saglayicilari: {}", providerMap.keySet());
    }

    public CreateShipmentResponse createShipment(CreateShipmentRequest request) {
        log.info("createShipment -> firm={}, referenceNo={}", request.getCargoFirm(), request.getReferenceNo());
        return resolve(request.getCargoFirm()).createShipment(request);
    }

    public CancelShipmentResponse cancelShipment(CancelShipmentRequest request) {
        log.info("cancelShipment -> firm={}, trackingNo={}", request.getCargoFirm(), request.getTrackingNo());
        return resolve(request.getCargoFirm()).cancelShipment(request);
    }

    public TrackShipmentResponse trackShipment(CargoFirm firm, String trackingNo) {
        log.info("trackShipment -> firm={}, trackingNo={}", firm, trackingNo);
        return resolve(firm).trackShipment(trackingNo);
    }

    private CargoProvider resolve(CargoFirm firm) {
        CargoProvider provider = providerMap.get(firm);
        if (provider == null) {
            throw new ApiException("Desteklenmeyen kargo firmasi: " + firm, 400);
        }
        return provider;
    }
}
