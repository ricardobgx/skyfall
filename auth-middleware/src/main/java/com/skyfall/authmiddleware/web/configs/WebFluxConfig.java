package com.skyfall.authmiddleware.web.configs;

import com.skyfall.authmiddleware.security.pojos.AuthMiddlewareSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
    @Autowired
    private AuthMiddlewareSecurityProperties securityProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        this.securityProperties.getCors().forEach((path, properties) ->
                registry.addMapping(path)
                        .allowedOrigins(properties.getAllowedOrigins().toArray(String[]::new))
                        .allowedMethods(properties.getAllowedMethods().toArray(String[]::new))
                        .allowedHeaders(properties.getAllowedHeaders().toArray(String[]::new))
                        .allowCredentials(properties.isAllowCredentials()));
    }
}
