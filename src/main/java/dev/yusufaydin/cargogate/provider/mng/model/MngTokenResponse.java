package dev.yusufaydin.cargogate.provider.mng.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngTokenResponse {
    @JsonProperty("jwt")
    private String jwt;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("jwtExpireDate")
    private String jwtExpireDate;

    @JsonProperty("refreshTokenExpireDate")
    private String refreshTokenExpireDate;
}
