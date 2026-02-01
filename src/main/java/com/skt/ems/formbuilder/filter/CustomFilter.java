package com.skt.ems.formbuilder.filter;

import com.skt.ems.common.dto.Response;
import com.skt.ems.formbuilder.client.user.UmsAuthClient;
import com.skt.ems.formbuilder.client.user.dto.request.TokenRequest;
import com.skt.ems.formbuilder.client.user.dto.response.AuthUserInfo;
import com.skt.ems.formbuilder.config.CustomContextDataUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    private final UmsAuthClient umsAuthClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        log.info("CustomFilter processing request: {} {}", request.getMethod(), url);

        CustomContextDataUtil.eraseCustomContextData();
        // Skip JWT processing for auth endpoints (login, register, etc.)
        if (url.startsWith("/public") || url.startsWith("/api/v1/form/")) {
            log.debug("Skipping JWT processing for /public endpoint: {}", url);
            filterChain.doFilter(request, response);
            return;
        }

        // Process JWT for protected endpoints
        final String authHeader = request.getHeader("Authorization");
        log.debug("Processing request for URL: {}, Auth header present: {}", url, authHeader != null);

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String jwtToken = authHeader.substring(7);
            log.debug("JWT token extracted, checking expiration");

            Response<AuthUserInfo> userInfo = umsAuthClient.parseToken(TokenRequest.builder().token(jwtToken).build());
            CustomContextDataUtil.setDataContext(userInfo.getData());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getData().getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.debug("Authentication set in SecurityContext");
        } else {
            log.debug("No Bearer token found in Authorization header");
        }
        filterChain.doFilter(request, response);
    }
}