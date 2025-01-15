package com.skyfall.authmiddleware.security.pojos;

import lombok.Data;

import java.util.List;

@Data
public class AuthMiddlewareSecurityCorsPathProperties {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private boolean allowCredentials;
}
