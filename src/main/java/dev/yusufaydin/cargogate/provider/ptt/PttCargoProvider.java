package dev.yusufaydin.cargogate.provider.ptt;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.ShipmentAddress;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;
import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import dev.yusufaydin.cargogate.gateway.provider.CargoProvider;
import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateFromClientRequest;
import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateFromCompanyRequest;
import dev.yusufaydin.cargogate.provider.ptt.model.PttCreateResponse;
import dev.yusufaydin.cargogate.provider.ptt.model.PttTrackResponse;
import dev.yusufaydin.cargogate.provider.ptt.model.PttUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * PTT saglayicisi.
 * Dogrudan SOAP atmaz; ptt-cargo-api'ye REST ile baglanir.
 * ptt-cargo-api -> SOAP -> PTT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PttCargoProvider implements CargoProvider {

    private final PttCargoClient pttCargoClient;

    @Override
    public CargoFirm supports() {
        return CargoFirm.PTT;
    }

    @Override
    public CreateShipmentResponse createShipment(CreateShipmentRequest request) {
        if (StringUtils.isNotBlank(request.getReferenceNo())) {
            return createFromClient(request);
        }
        return createFromCompany(request);
    }

    private CreateShipmentResponse createFromClient(CreateShipmentRequest request) {
        log.info("PTT from-client -> referenceNo={}", request.getReferenceNo());
        PttCreateFromClientRequest req = PttCreateFromClientRequest.builder()
                .receiver(toUserInfo(request.getReceiver()))
                .sender(toUserInfo(request.getSender()))
                .refId(request.getReferenceNo())
                .build();
        PttCreateResponse response = pttCargoClient.createFromClient(req);
        assertSuccess(response.getStatus(), response.getError());
        return CreateShipmentResponse.builder()
                .labelUrl(response.getData().getUrl())
                .referenceNo(request.getReferenceNo())
                .build();
    }

    private CreateShipmentResponse createFromCompany(CreateShipmentRequest request) {
        log.info("PTT from-company -> otomatik barkod uretilecek");
        PttCreateFromCompanyRequest req = PttCreateFromCompanyRequest.builder()
                .receiver(toUserInfo(request.getReceiver()))
                .sender(toUserInfo(request.getSender()))
                .build();
        PttCreateResponse response = pttCargoClient.createFromCompany(req);
        assertSuccess(response.getStatus(), response.getError());
        String barcode = Optional.ofNullable(response.getData().getBarcode())
                .map(String::valueOf).orElse(null);
        return CreateShipmentResponse.builder()
                .labelUrl(response.getData().getUrl())
                .barcode(barcode)
                .build();
    }

    @Override
    public CancelShipmentResponse cancelShipment(CancelShipmentRequest request) {
        throw new ApiException("PTT icin iptal desteklenmiyor. ptt-cargo-api'de iptal endpoint'i tanimli degil.", 501);
    }

    @Override
    public TrackShipmentResponse trackShipment(String trackingNo) {
        log.info("PTT track -> refNo={}", trackingNo);
        PttTrackResponse response = pttCargoClient.track(trackingNo);
        assertSuccess(response.getStatus(), response.getError());
        PttTrackResponse.Data data = response.getData();
        List<TrackShipmentResponse.TrackingEvent> events = Optional.ofNullable(data.getHistories())
                .orElse(Collections.emptyList())
                .stream()
                .map(h -> TrackShipmentResponse.TrackingEvent.builder()
                        .date(h.getDate())
                        .time(h.getTime())
                        .location(h.getProcessingCenter())
                        .description(h.getStatus())
                        .order(h.getOrder())
                        .build())
                .toList();
        return TrackShipmentResponse.builder()
                .trackingNo(trackingNo)
                .senderName(data.getSender())
                .receiverName(data.getReceiver())
                .processingCenter(data.getProcessingCenter())
                .destinationCenter(data.getDestinationCenter())
                .events(events)
                .build();
    }

    private PttUserInfo toUserInfo(ShipmentAddress addr) {
        return PttUserInfo.builder()
                .firstName(addr.getFirstName())
                .surname(addr.getLastName())
                .phone(addr.getPhone())
                .address(addr.getAddress())
                .city(addr.getCity())
                .town(addr.getDistrict())
                .build();
    }

    private void assertSuccess(int status, String error) {
        if (status != 200) {
            throw new ApiException("PTT hatasi: " + error, 502);
        }
    }
}
