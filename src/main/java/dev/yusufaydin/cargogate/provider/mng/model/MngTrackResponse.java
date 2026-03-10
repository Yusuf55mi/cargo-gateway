package dev.yusufaydin.cargogate.provider.mng.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * MNG TrackShipment response item.
 * matches definitions/TrackShipmentResponse in Swagger.
 */
@Getter
@Setter
public class MngTrackResponse {
    @JsonProperty("referenceId")
    private String referenceId;

    @JsonProperty("shipmentId")
    private String shipmentId;

    @JsonProperty("eventSequence")
    private String eventSequence;

    @JsonProperty("eventStatus")
    private String eventStatus;

    @JsonProperty("eventStatusEn")
    private String eventStatusEn;

    @JsonProperty("eventDateTime")
    private String eventDateTime;

    @JsonProperty("eventDateTimeFormat")
    private String eventDateTimeFormat;

    @JsonProperty("eventDateTimezone")
    private String eventDateTimezone;

    @JsonProperty("location")
    private String location;

    @JsonProperty("country")
    private String country;

    @JsonProperty("locationAddress")
    private String locationAddress;

    @JsonProperty("locationPhone")
    private String locationPhone;

    @JsonProperty("deliveryDateTime")
    private String deliveryDateTime;

    @JsonProperty("deliveryTo")
    private String deliveryTo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("pieceTotal")
    private String pieceTotal;
}
