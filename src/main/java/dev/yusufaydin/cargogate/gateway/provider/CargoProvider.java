package dev.yusufaydin.cargogate.gateway.provider;

import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;

public interface CargoProvider {
    CargoFirm supports();
    CreateShipmentResponse createShipment(CreateShipmentRequest request);
    CancelShipmentResponse cancelShipment(CancelShipmentRequest request);
    TrackShipmentResponse trackShipment(String trackingNo);
}
