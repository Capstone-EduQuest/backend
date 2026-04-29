package com.eduquest.backend.infrastructure.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    public static final String REFRESH_TOKEN = "refreshToken";

    public Cookie createRefreshTokenCookie(String refreshToken, long expiration) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (expiration / 1000));
        cookie.setHttpOnly(true);
        // Todo : https 적용 시 secure 옵션 활성화
        //cookie.setSecure(true);
        return cookie;
    }

    public Cookie emptyRefreshCookie() {
        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (REFRESH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
