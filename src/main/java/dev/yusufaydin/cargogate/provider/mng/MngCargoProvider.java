package dev.yusufaydin.cargogate.provider.mng;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.common.property.MngConfigProperty;
import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.ShipmentAddress;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;
import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import dev.yusufaydin.cargogate.gateway.provider.CargoProvider;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderRequest;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderRequest.MngOrder;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderRequest.MngOrderPiece;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderRequest.MngRecipient;
import dev.yusufaydin.cargogate.provider.mng.model.MngCreateOrderResponse;
import dev.yusufaydin.cargogate.provider.mng.model.MngTrackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MNG Kargo saglayicisi.
 * cargo-gateway -> REST/JSON + Bearer token -> MNG API
 *
 * Sandbox:  https://sandbox.mngkargo.com.tr
 * Uretim:   https://apizone.mngkargo.com.tr
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MngCargoProvider implements CargoProvider {

    private final MngCargoClient mngCargoClient;
    private final MngTokenService mngTokenService;
    private final MngConfigProperty config;

    @Override
    public CargoFirm supports() {
        return CargoFirm.MNG;
    }

    @Override
    public CreateShipmentResponse createShipment(CreateShipmentRequest request) {
        String referenceId = StringUtils.isNotBlank(request.getReferenceNo())
                ? request.getReferenceNo()
                : UUID.randomUUID().toString();
        log.info("MNG createOrder -> referenceId={}", referenceId);

        BigDecimal amount = request.getCollectionAmount();
        boolean isCod = amount != null && amount.compareTo(BigDecimal.ZERO) > 0;

        MngCreateOrderRequest orderRequest = MngCreateOrderRequest.builder()
                .order(MngOrder.builder()
                        .referenceId(referenceId)
                        .barcode(referenceId)
                        .billOfLandingId("")
                        .isCOD(isCod ? 1 : 0)
                        .codAmount(isCod ? amount.doubleValue() : 0)
                        .shipmentServiceType(1)
                        .packagingType(3)
                        .content("Kargo")
                        .smsPreference1(0).smsPreference2(0).smsPreference3(0)
                        .paymentType(1)
                        .deliveryType(1)
                        .description("")
                        .marketPlaceShortCode("").marketPlaceSaleCode("")
                        .build())
                .orderPieceList(List.of(MngOrderPiece.builder()
                        .barcode(referenceId)
                        .desi(request.getDesi() != null ? request.getDesi() : 1)
                        .kg(request.getDesi() != null ? request.getDesi() : 1)
                        .content("Parca 1")
                        .build()))
                .recipient(toRecipient(request.getReceiver()))
                .build();

        MngCreateOrderResponse response = mngCargoClient.createOrder(
                bearer(), config.getClientId(), config.getClientSecret(), orderRequest);

        if (response == null) {
            throw new ApiException("MNG cevabi bos dondu", 502);
        }

        return CreateShipmentResponse.builder()
                .trackingNo(response.getOrderInvoiceId())
                .barcode(response.getReferenceId())
                .referenceNo(response.getReferenceId())
                .build();
    }

    @Override
    public CancelShipmentResponse cancelShipment(CancelShipmentRequest request) {
        log.info("MNG cancelOrder -> referenceId={}", request.getTrackingNo());
        mngCargoClient.cancelOrder(
                bearer(), config.getClientId(), config.getClientSecret(),
                request.getTrackingNo());
        return CancelShipmentResponse.builder()
                .trackingNo(request.getTrackingNo())
                .message("Iptal talebi gonderildi")
                .build();
    }

    @Override
    public TrackShipmentResponse trackShipment(String trackingNo) {
        log.info("MNG trackShipment -> referenceNo={}", trackingNo);
        List<MngTrackResponse> responses = mngCargoClient.trackByReference(
                bearer(), config.getClientId(), config.getClientSecret(), trackingNo);

        if (responses == null || responses.isEmpty()) {
            throw new ApiException("MNG takip bilgisi bulunamadi: " + trackingNo, 404);
        }

        MngTrackResponse lastEvent = responses.get(responses.size() - 1);

        List<TrackShipmentResponse.TrackingEvent> events = responses.stream()
                .map(d -> TrackShipmentResponse.TrackingEvent.builder()
                        .date(d.getEventDateTime())
                        .time("")
                        .location(d.getLocation())
                        .description(d.getEventStatus())
                        .build())
                .toList();

        return TrackShipmentResponse.builder()
                .trackingNo(trackingNo)
                .status(lastEvent.getEventStatus())
                .senderName("")
                .receiverName("")
                .events(events)
                .build();
    }

    private String bearer() {
        return "Bearer " + mngTokenService.getToken();
    }

    private MngRecipient toRecipient(ShipmentAddress addr) {
        return MngRecipient.builder()
                .customerId("").refCustomerId("")
                .cityCode(0).cityName(addr.getCity())
                .districtCode(0).districtName(addr.getDistrict())
                .address(addr.getAddress())
                .fullName(addr.getFirstName() + " " + addr.getLastName())
                .mobilePhoneNumber(addr.getPhone())
                .bussinessPhoneNumber("").homePhoneNumber("")
                .email("").taxOffice("").taxNumber("")
                .build();
    }
}
