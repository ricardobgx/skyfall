package com.skyfall.authmiddleware.security.pojos;

import lombok.Data;

@Data
public class AuthMiddlewareSecurityOAuth2Properties {
    private AuthMiddlewareSecurityOAuth2ClientProperties client;
    private AuthMiddlewareSecurityOAuth2ResourceServerProperties resourceServer;
}
