package dev.yusufaydin.cargogate.provider.yurtici;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.common.property.YurticiConfigProperty;
import dev.yusufaydin.cargogate.gateway.model.request.CancelShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.CreateShipmentRequest;
import dev.yusufaydin.cargogate.gateway.model.request.ShipmentAddress;
import dev.yusufaydin.cargogate.gateway.model.response.CancelShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.CreateShipmentResponse;
import dev.yusufaydin.cargogate.gateway.model.response.TrackShipmentResponse;
import dev.yusufaydin.cargogate.gateway.provider.CargoFirm;
import dev.yusufaydin.cargogate.gateway.provider.CargoProvider;
import dev.yusufaydin.cargogate.provider.yurtici.model.YurticiCreateResponse;
import dev.yusufaydin.cargogate.provider.yurtici.model.YurticiQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Yurtici Kargo saglayicisi.
 * cargo-gateway -> SOAP/XML -> Yurtici web servisi
 *
 * ONEMLI:
 *   - queryShipment: ayni barkod icin 1 dakika icinde tekrar sorgu yapilamaz.
 *   - IP Whitelist: Yurtici'nin sunucu IP'nizi whitelist'e almasi gerekir.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class YurticiCargoProvider implements CargoProvider {

    private final YurticiCargoClient yurticiCargoClient;
    private final YurticiXmlService yurticiXmlService;
    private final YurticiConfigProperty config;

    @Override
    public CargoFirm supports() {
        return CargoFirm.YURTICI;
    }

    @Override
    public CreateShipmentResponse createShipment(CreateShipmentRequest request) {
        if (request.getDesi() == null || request.getDesi() <= 0) {
            throw new ApiException("Yurtici icin desi alani zorunludur ve 0'dan buyuk olmalidir.");
        }
        log.info("Yurtici createShipment -> desi={}", request.getDesi());

        Map<String, Object> model = new HashMap<>();
        model.put("username", config.getUsername());
        model.put("password", config.getPassword());
        model.put("userLanguage", config.getUserLanguage());
        model.put("desi", request.getDesi());
        model.put("cargoCount", 1);
        model.put("referenceNo", request.getReferenceNo() != null ? request.getReferenceNo() : "");
        String cargoKey = request.getReferenceNo() != null 
            ? request.getReferenceNo() 
            : UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        model.put("cargoKey", cargoKey);
        model.put("receiver", request.getReceiver());
        model.put("sender", request.getSender());

        // Kapida tahsilat
        BigDecimal amount = request.getCollectionAmount();
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            model.put("ttCollectionPrice", amount.toPlainString());
            boolean isCard = request.getCollectionType() == CreateShipmentRequest.CollectionType.CREDIT_CARD;
            model.put("ttCollectionType", isCard ? "1" : "0");
            model.put("hasCOD", true);
        } else {
            model.put("ttCollectionPrice", "0");
            model.put("ttCollectionType", "-1");
            model.put("hasCOD", false);
        }

        String soapXml = yurticiXmlService.generateSoapXml("createShipment.ftl", model);
        String xmlResponse = yurticiCargoClient.call(soapXml);
        YurticiCreateResponse response = yurticiXmlService.parseCreateResponse(xmlResponse);

        if (!"0".equals(response.getResultCode())) {
            throw new ApiException("Yurtici hatasi: " + response.getResultMessage(), 502);
        }

        return CreateShipmentResponse.builder()
                .barcode(response.getBarcode())
                .trackingNo(response.getBarcode())
                .referenceNo(request.getReferenceNo())
                .build();
    }

    @Override
    public CancelShipmentResponse cancelShipment(CancelShipmentRequest request) {
        log.info("Yurtici cancelShipment -> trackingNo={}", request.getTrackingNo());

        Map<String, Object> model = new HashMap<>();
        model.put("username", config.getUsername());
        model.put("password", config.getPassword());
        model.put("userLanguage", config.getUserLanguage());
        model.put("trackingNo", request.getTrackingNo());

        String soapXml = yurticiXmlService.generateSoapXml("cancelShipment.ftl", model);
        String xmlResponse = yurticiCargoClient.call(soapXml);

        // Basarili cancel yaniti "0" resultCode icerir
        if (!xmlResponse.contains(">0<") && !xmlResponse.contains(">CNL<") && !xmlResponse.contains("SUCCESS")) {
            log.warn("Yurtici cancel yaniti beklenmedik: {}", xmlResponse);
            throw new ApiException("Yurtici iptal basarisiz. Yanit: " + xmlResponse, 502);
        }

        return CancelShipmentResponse.builder()
                .trackingNo(request.getTrackingNo())
                .message("Iptal islemi basarili")
                .build();
    }

    @Override
    public TrackShipmentResponse trackShipment(String trackingNo) {
        log.info("Yurtici queryShipment -> trackingNo={}", trackingNo);

        Map<String, Object> model = new HashMap<>();
        model.put("username", config.getUsername());
        model.put("password", config.getPassword());
        model.put("userLanguage", config.getUserLanguage());
        model.put("trackingNo", trackingNo);

        String soapXml = yurticiXmlService.generateSoapXml("queryShipment.ftl", model);
        String xmlResponse = yurticiCargoClient.call(soapXml);
        YurticiQueryResponse response = yurticiXmlService.parseQueryResponse(xmlResponse);

        List<TrackShipmentResponse.TrackingEvent> events = Optional.ofNullable(response.getEvents())
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> TrackShipmentResponse.TrackingEvent.builder()
                        .date(e.getDate())
                        .time(e.getTime())
                        .location(e.getLocation())
                        .description(e.getDescription())
                        .build())
                .toList();

        return TrackShipmentResponse.builder()
                .trackingNo(trackingNo)
                .status(resolveStatus(response.getOperationStatus()))
                .senderName(response.getSenderName())
                .receiverName(response.getReceiverName())
                .events(events)
                .build();
    }

    private String resolveStatus(String code) {
        if (code == null) return "BILINMIYOR";
        return switch (code) {
            case "NOP" -> "Islemde";
            case "IND" -> "Indirildi";
            case "ISR" -> "Israr";
            case "CNL" -> "Iptal";
            case "ISC" -> "Iptal Sonuclandi";
            case "DLV" -> "Teslim Edildi";
            case "BI"  -> "Bilgi Girisi";
            default    -> code;
        };
    }
}
