package dev.yusufaydin.cargogate.provider.ptt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ptt-cargo-api GeneralResponse<GonderiSorguResponse> sablonu
 */
@Getter
@Setter
public class PttTrackResponse {
    private int status;
    private String error;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String refNo;
        private String receiver;
        private String sender;
        private String processingCenter;
        private String destinationCenter;
        private String date;
        private String barcodeNo;
        private Integer count;
        private String url;
        private List<HistoryItem> histories;
    }

    @Getter
    @Setter
    public static class HistoryItem {
        private String time;
        private Integer order;
        private String status;
        private String processingCenter;
        private String date;
    }
}
