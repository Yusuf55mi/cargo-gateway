package dev.yusufaydin.cargogate.provider.mng.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * MNG CreateOrder request.
 * CreateOrder.txt referans alinarak olusturuldu.
 */
@Getter
@Builder
public class MngCreateOrderRequest {

    private MngOrder order;
    private List<MngOrderPiece> orderPieceList;
    private MngRecipient recipient;

    @Getter
    @Builder
    public static class MngOrder {
        private String referenceId;
        private String barcode;
        private String billOfLandingId;
        private int isCOD;
        private double codAmount;
        private int shipmentServiceType;
        private int packagingType;
        private String content;
        private int smsPreference1;
        private int smsPreference2;
        private int smsPreference3;
        private int paymentType;
        private int deliveryType;
        private String description;
        private String marketPlaceShortCode;
        private String marketPlaceSaleCode;
    }

    @Getter
    @Builder
    public static class MngOrderPiece {
        private String barcode;
        private int desi;
        private int kg;
        private String content;
    }

    @Getter
    @Builder
    public static class MngRecipient {
        private String customerId;
        private String refCustomerId;
        private int cityCode;
        private String cityName;
        private int districtCode;
        private String districtName;
        private String address;
        private String bussinessPhoneNumber;
        private String email;
        private String taxOffice;
        private String taxNumber;
        private String fullName;
        private String homePhoneNumber;
        private String mobilePhoneNumber;
    }
}
