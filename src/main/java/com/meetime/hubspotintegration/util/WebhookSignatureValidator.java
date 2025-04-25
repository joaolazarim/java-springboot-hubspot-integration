package com.meetime.hubspotintegration.util;

import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

@Component
public class WebhookSignatureValidator {

    private static final Logger logger = LoggerFactory.getLogger(WebhookSignatureValidator.class);

    private static final String HMAC_ALGO = "HmacSHA256";
    private final String clientSecret;

    public WebhookSignatureValidator(HubSpotProperties props) {
        this.clientSecret = props.getClientSecret();
    }

    public boolean isValid(HttpServletRequest req, String rawBody) {
        String version = req.getHeader("X-HubSpot-Signature-Version");
        String sigV1or2  = req.getHeader("X-HubSpot-Signature");
        String sigV3 = req.getHeader("X-HubSpot-Signature-V3");
        String timestamp = req.getHeader("X-HubSpot-Request-Timestamp");
        String method = req.getMethod();
        String uri = buildFullUri(req);

        logger.info("Signature version: {}", version);

        if ("v3".equalsIgnoreCase(version)) {
            return validateV3(sigV3, timestamp, method, uri, rawBody);
        }
        if ("v2".equalsIgnoreCase(version)) {
            return validateV2(sigV1or2, method, uri, rawBody);
        }

        return validateV1(sigV1or2, rawBody);
    }

    private boolean validateV1(String signature, String body) {
        String expected = sha256Hex(clientSecret + body);

        return MessageDigest.isEqual(expected.getBytes(), signature.getBytes());
    }

    private boolean validateV2(String signature,
                               String method,
                               String uri,
                               String body) {
        String source   = clientSecret + method + uri + body;
        String expected = sha256Hex(source);

        return MessageDigest.isEqual(expected.getBytes(), signature.getBytes());
    }

    private boolean validateV3(String signature, String timestamp, String method, String uri, String body) {
        long ts = Long.parseLong(timestamp);

        if (Math.abs(Instant.now().getEpochSecond() - ts) > 5 * 60) {
            logger.error(ErrorMessageEnum.SIGNATURE_TIMESTAMP_TOO_OLD.getMessage());
            return false;
        }

        String decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
        String source = method + decodedUri + body + timestamp;

        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(clientSecret.getBytes(), HMAC_ALGO));
            byte[] raw = mac.doFinal(source.getBytes(StandardCharsets.UTF_8));
            String expected = Base64.getEncoder().encodeToString(raw);

            return MessageDigest.isEqual(expected.getBytes(), signature.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    private String sha256Hex(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildFullUri(HttpServletRequest req) {
        String query = req.getQueryString();
        return req.getRequestURL() + (query != null ? "?" + query : "");
    }
}