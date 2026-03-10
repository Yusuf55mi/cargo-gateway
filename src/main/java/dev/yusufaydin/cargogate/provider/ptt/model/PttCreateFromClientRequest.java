package dev.yusufaydin.cargogate.provider.ptt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PttCreateFromClientRequest {
    private PttUserInfo receiver;
    private PttUserInfo sender;
    private String refId;
}
