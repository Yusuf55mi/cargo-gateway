package dev.yusufaydin.cargogate.provider.yurtici.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Yurtici queryShipment SOAP yaniti.
 * XML'den manuel parse edilir (YurticiXmlService.parseQueryResponse).
 *
 * operationStatus kodlari:
 *   NOP = Islemde
 *   IND = Indirildi
 *   ISR = Israr
 *   CNL = Iptal
 *   ISC = Iptal Sonuclandi
 *   DLV = Teslim Edildi
 *   BI  = Bilgi Girisi
 */
@Getter
@Setter
public class YurticiQueryResponse {
    private String operationStatus;
    private String senderName;
    private String receiverName;
    private List<YurticiTrackEvent> events;
}
