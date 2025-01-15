package com.skyfall.authmiddleware.security.pojos;

import lombok.Data;

import java.util.List;

@Data
public class AuthMiddlewareSecurityOAuth2ClientProperties {
    private List<String> publicRoutes;
    private List<String> privateRoutes;
}
