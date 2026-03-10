package dev.yusufaydin.cargogate.provider.yurtici.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Yurtici createShipment SOAP yaniti.
 * XML'den manuel parse edilir (YurticiXmlService.parseCreateResponse).
 */
@Getter
@Setter
public class YurticiCreateResponse {
    /** "0" = basarili */
    private String resultCode;
    private String resultMessage;
    /** Uretilen barkod */
    private String barcode;
}
