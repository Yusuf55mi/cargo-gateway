package dev.yusufaydin.cargogate.provider.mng.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MngTokenRequest {
    private final String customerNumber;
    private final String password;
    private final int identityType;

    public MngTokenRequest(String customerNumber, String password) {
        this.customerNumber = customerNumber;
        this.password = password;
        this.identityType = 1;
    }

    @Builder
    public MngTokenRequest(String customerNumber, String password, int identityType) {
        this.customerNumber = customerNumber;
        this.password = password;
        this.identityType = identityType == 0 ? 1 : identityType;
    }
}
