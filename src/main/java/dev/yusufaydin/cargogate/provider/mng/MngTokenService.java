package dev.yusufaydin.cargogate.provider.mng;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.common.property.MngConfigProperty;
import dev.yusufaydin.cargogate.provider.mng.model.MngTokenRequest;
import dev.yusufaydin.cargogate.provider.mng.model.MngTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * MNG JWT token cache servisi.
 * Token 8 saat gecerlidir, 1 dk oncesinde otomatik yenilenir.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MngTokenService {

    private final MngAuthClient mngAuthClient;
    private final MngConfigProperty config;

    private String cachedToken;
    private Instant tokenExpiresAt;

    public synchronized String getToken() {
        if (isValid()) {
            return cachedToken;
        }
        return refresh();
    }

    private boolean isValid() {
        return cachedToken != null
                && tokenExpiresAt != null
                && Instant.now().isBefore(tokenExpiresAt.minusSeconds(60));
    }

    private String refresh() {
        log.info("MNG token yenileniyor...");
        try {
            MngTokenResponse response = mngAuthClient.getToken(
                    config.getClientId(),
                    config.getClientSecret(),
                    new MngTokenRequest(config.getCustomerNumber(), config.getPassword())
            );
            if (response == null || response.getJwt() == null) {
                throw new ApiException("MNG token alinamadi: bos yanit", 502);
            }
            cachedToken = response.getJwt();
            tokenExpiresAt = Instant.now().plusSeconds((long) config.getTokenTtlMinutes() * 60);
            log.info("MNG token alindi, gecerlilik: {} dk", config.getTokenTtlMinutes());
            return cachedToken;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("MNG token hatasi: {}", e.getMessage());
            throw new ApiException("MNG token alinamadi: " + e.getMessage(), 502);
        }
    }
}
