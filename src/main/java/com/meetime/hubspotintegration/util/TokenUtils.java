package com.meetime.hubspotintegration.util;

import com.meetime.hubspotintegration.enums.ErrorMessageEnum;

public class TokenUtils {

    public static String getToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessageEnum.MISSING_TOKEN.getMessage());
        }
        return token.replace("Bearer ", "");
    }
}
