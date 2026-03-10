package dev.yusufaydin.cargogate.provider.yurtici.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YurticiTrackEvent {
    private String date;
    private String time;
    private String location;
    private String description;
}
