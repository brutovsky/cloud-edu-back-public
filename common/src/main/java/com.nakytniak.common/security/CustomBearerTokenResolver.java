package com.nakytniak.common.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.logging.Logger;

public class CustomBearerTokenResolver implements BearerTokenResolver {

    private final Logger logger = Logger.getLogger(CustomBearerTokenResolver.class.getName());

    private static final String API_GATEWAY_JWT_PAYLOAD_HEADER_NAME = "X-Endpoint-API-UserInfo";
    private static final String API_GATEWAY_FORWARDED_AUTHENTICATION_HEADER_NAME = "X-Forwarded-Authorization";

    @SuppressWarnings("LineLength")
    // Info on X-Forwarded-Authorization:
    //https://stackoverflow.com/questions/65952381/google-api-gateway-firebase-x-apigateway-api-userinfo-vs-x-forwarded-authoriz
    // Different headers on different envs
    @Override
    public String resolve(final HttpServletRequest request) {
        final int bearerHeaderLength = 7;
        final String authorization = request.getHeader(API_GATEWAY_FORWARDED_AUTHENTICATION_HEADER_NAME);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(bearerHeaderLength);
    }

}
