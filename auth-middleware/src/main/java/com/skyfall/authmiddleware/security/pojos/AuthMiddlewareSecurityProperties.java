package com.skyfall.authmiddleware.security.pojos;

import lombok.Data;

import java.util.Map;

@Data
public class AuthMiddlewareSecurityProperties {
    private AuthMiddlewareSecurityOAuth2Properties oauth2;
    private Map<String, AuthMiddlewareSecurityCorsPathProperties> cors;
}
