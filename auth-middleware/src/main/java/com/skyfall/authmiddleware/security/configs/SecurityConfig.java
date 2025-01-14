package com.skyfall.authmiddleware.security.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {
    }

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));

            var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));

            return roles.stream().flatMap(Collection::stream)
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        };
    }

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper(
            Converter<Map<String, Object>, Collection<GrantedAuthority>> authoritiesConverter) {
        return (authorities) -> authorities.stream()
                .filter(authority -> authority instanceof OidcUserAuthority)
                .map(OidcUserAuthority.class::cast)
                .map(OidcUserAuthority::getIdToken)
                .map(OidcIdToken::getClaims)
                .map(authoritiesConverter::convert)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Bean
    public SecurityWebFilterChain clientSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("{baseUrl}/")
                        .logoutSuccessHandler(((exchange, authentication) -> {
                             ServerHttpResponse response = exchange
                                     .getExchange()
                                     .getResponse();

                             response.setStatusCode(HttpStatusCode.valueOf(201));

                             return response.setComplete();
                        })))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/**", "/login/**", "/oauth2/**", "/favicon.ico").permitAll()
                        .pathMatchers("/logout").authenticated()
                        .anyExchange().denyAll())
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public SecurityWebFilterChain resourceServerSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> withDefaults()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(requests -> requests
                        .pathMatchers(
                                "/login-options",
                                "/error",
                                "/actuator/health/readiness",
                                "/actuator/health/liveness"
                        ).permitAll())
                .build();
    }
}
