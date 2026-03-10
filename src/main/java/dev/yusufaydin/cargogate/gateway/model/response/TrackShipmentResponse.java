package dev.yusufaydin.cargogate.gateway.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackShipmentResponse {
    private String trackingNo;
    private String status;
    private String senderName;
    private String receiverName;
    private String processingCenter;
    private String destinationCenter;
    private List<TrackingEvent> events;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TrackingEvent {
        private String date;
        private String time;
        private String location;
        private String description;
        private Integer order;
    }
}
