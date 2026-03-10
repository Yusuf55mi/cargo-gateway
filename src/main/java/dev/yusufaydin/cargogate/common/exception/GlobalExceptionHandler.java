package dev.yusufaydin.cargogate.common.exception;

import dev.yusufaydin.cargogate.common.model.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<GeneralResponse<Void>> handleApiException(ApiException ex) {
        log.error("ApiException: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(GeneralResponse.<Void>builder()
                        .status(ex.getStatusCode())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GeneralResponse.<Void>builder()
                        .status(400)
                        .error(errors)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse<Void>> handleGeneral(Exception ex) {
        log.error("Beklenmeyen hata: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GeneralResponse.<Void>builder()
                        .status(500)
                        .error("Beklenmeyen bir hata olustu")
                        .build());
    }
}
