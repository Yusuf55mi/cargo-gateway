package dev.yusufaydin.cargogate.provider.ptt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PttCreateFromCompanyRequest {
    private PttUserInfo receiver;
    private PttUserInfo sender;
}
