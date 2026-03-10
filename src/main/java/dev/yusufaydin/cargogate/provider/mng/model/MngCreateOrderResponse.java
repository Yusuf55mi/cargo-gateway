package dev.yusufaydin.cargogate.provider.mng.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MngCreateOrderResponse {
    @JsonProperty("orderInvoiceId")
    private String orderInvoiceId;

    @JsonProperty("orderInvoiceDetailId")
    private String orderInvoiceDetailId;

    @JsonProperty("shipperBranchCode")
    private String shipperBranchCode;

    @JsonProperty("referenceId")
    private String referenceId;
}
