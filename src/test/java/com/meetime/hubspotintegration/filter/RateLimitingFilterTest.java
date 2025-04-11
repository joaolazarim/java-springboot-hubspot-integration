package com.meetime.hubspotintegration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RateLimitingFilterTest {

    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    public void setup() {
        rateLimitingFilter = new RateLimitingFilter();
    }

    @Test
    public void testRateLimitAllowsRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        when(request.getRequestURI()).thenReturn("/contacts");
        when(request.getHeader("Authorization")).thenReturn("Bearer testToken");

        rateLimitingFilter.doFilter(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void testGlobalRateLimitExceeds() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        when(request.getRequestURI()).thenReturn("/contacts");
        when(request.getHeader("Authorization")).thenReturn("Bearer testToken");

        for (int i = 0; i < 101; i++) {
            rateLimitingFilter.doFilter(request, response, chain);
        }

        verify(chain, atMost(100)).doFilter(request, response);
    }

    @Test
    public void testAccountRateLimitExceeds() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        when(request.getRequestURI()).thenReturn("/contacts");
        when(request.getHeader("Authorization")).thenReturn("Bearer accountTestToken");

        rateLimitingFilter.doFilter(request, response, chain);
        rateLimitingFilter.doFilter(request, response, chain);

        verify(chain, atLeast(1)).doFilter(request, response);
    }
}