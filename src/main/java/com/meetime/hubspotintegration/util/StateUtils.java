package com.meetime.hubspotintegration.util;

import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.exception.StateSignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class StateUtils {

    private static final String HMAC_SPEC = "HmacSHA256";
    private final String secret;

    public StateUtils(HubSpotProperties hubSpotProperties) {
        this.secret = hubSpotProperties.getClientSecret();
    }

    public String generateSignedState(String state) {
        try {
            Mac mac = Mac.getInstance(HMAC_SPEC);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_SPEC);
            mac.init(secretKey);
            byte[] signatureBytes = mac.doFinal(state.getBytes());
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            return state + "." + signature;
        } catch (Exception e) {
            throw new StateSignatureException(ErrorMessageEnum.ERROR_SIGNING_STATE.getMessage(), e);
        }
    }

    public boolean verifySignedState(String signedState) {
        if (signedState == null || !signedState.contains(".")) {
            return false;
        }
        String[] parts = signedState.split("\\.");
        if (parts.length != 2) {
            return false;
        }
        String state = parts[0];
        String signatureProvided = parts[1];
        String expectedSignature = generateSignedState(state).split("\\.")[1];
        return expectedSignature.equals(signatureProvided);
    }
}