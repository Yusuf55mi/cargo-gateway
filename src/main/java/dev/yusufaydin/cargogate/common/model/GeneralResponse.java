package dev.yusufaydin.cargogate.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse<T> {
    @Builder.Default
    private int status = 200;
    private String error;
    private T data;
}
