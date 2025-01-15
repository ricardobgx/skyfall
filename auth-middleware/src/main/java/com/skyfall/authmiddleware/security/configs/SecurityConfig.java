package com.skyfall.authmiddleware.security.configs;

import com.skyfall.authmiddleware.security.interfaces.AuthoritiesConverter;
import com.skyfall.authmiddleware.security.pojos.AuthMiddlewareSecurityOAuth2ClientProperties;
import com.skyfall.authmiddleware.security.pojos.AuthMiddlewareSecurityProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
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
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    @ConfigurationProperties(prefix = "skyfall.auth-middleware.security")
    public AuthMiddlewareSecurityProperties authMiddlewareSecurityProperties() {
        return new AuthMiddlewareSecurityProperties();
    }

    @Bean
    @SuppressWarnings("unchecked")
    public AuthoritiesConverter realmRolesAuthoritiesConverter() {
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
    public SecurityWebFilterChain clientSecurityFilterChain(
            ServerHttpSecurity httpSecurity,
            AuthMiddlewareSecurityProperties securityProperties
    ) {
        AuthMiddlewareSecurityOAuth2ClientProperties oauth2ClientProperties = securityProperties
                .getOauth2()
                .getClient();

        List<String> publicRoutes = oauth2ClientProperties.getPublicRoutes();
        List<String> privateRoutes = oauth2ClientProperties.getPrivateRoutes();

        List<String> oauth2ClientRoutes = new ArrayList<>(publicRoutes);

        oauth2ClientRoutes.addAll(privateRoutes);

        ServerWebExchangeMatcher[] securityMatchers = oauth2ClientRoutes.stream()
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .toArray(PathPatternParserServerWebExchangeMatcher[]::new);

        return httpSecurity
                .securityMatcher(new OrServerWebExchangeMatcher(securityMatchers))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(publicRoutes.toArray(String[]::new)).permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("{baseUrl}/")
                        .logoutSuccessHandler(((exchange, authentication) -> exchange
                                .getExchange()
                                .getResponse()
                                .setComplete()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public SecurityWebFilterChain resourceServerSecurityFilterChain(
            ServerHttpSecurity httpSecurity,
            AuthMiddlewareSecurityProperties securityProperties
    ) {
        List<String> publicRoutes = securityProperties
                .getOauth2()
                .getResourceServer()
                .getPublicRoutes();

        return httpSecurity
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(publicRoutes.toArray(String[]::new)).permitAll()
                        .anyExchange().denyAll())
                .build();
    }
}
