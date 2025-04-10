package com.meetime.hubspotintegration.filter;

import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.meetime.hubspotintegration.util.TokenUtils.getToken;

@Component
public class RateLimitingFilter implements Filter {

    private final Bucket globalBucket;
    private final Map<String, Bucket> accountBuckets = new ConcurrentHashMap<>();

    public RateLimitingFilter() {
        Bandwidth globalLimit = Bandwidth.simple(100, Duration.ofSeconds(10));
        this.globalBucket = Bucket4j.builder()
                .addLimit(globalLimit)
                .build();
    }

    private Bucket resolveAccountBucket(String token) {
        return accountBuckets.computeIfAbsent(token, key -> {
            Bandwidth dailyLimit = Bandwidth.simple(250000, Duration.ofDays(1));
            return Bucket4j.builder()
                    .addLimit(dailyLimit)
                    .build();
        });
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (path.startsWith("/contacts")) {
            if (!globalBucket.tryConsume(1)) {
                httpResponse.setStatus(429);
                httpResponse.getWriter().write(ErrorMessageEnum.TOO_MANY_REQUESTS.getMessage());
                return;
            }

            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = getToken(authHeader);
                Bucket accountBucket = resolveAccountBucket(token);
                if (!accountBucket.tryConsume(1)) {
                    httpResponse.setStatus(429);
                    httpResponse.getWriter().write(ErrorMessageEnum.TOO_MANY_REQUESTS.getMessage());
                    return;
                }
            } else {
                httpResponse.setStatus(401);
                httpResponse.getWriter().write(ErrorMessageEnum.MISSING_TOKEN.getMessage());
                return;
            }
        }

        chain.doFilter(request, response);
    }
}