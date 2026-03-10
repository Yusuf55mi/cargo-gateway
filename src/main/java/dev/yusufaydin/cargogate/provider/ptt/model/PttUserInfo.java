package dev.yusufaydin.cargogate.provider.ptt.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PttUserInfo {
    private String firstName;
    private String surname;
    private String phone;
    private String address;
    private String city;
    private String town;
}
