package com.skyfall.authmiddleware.security.controllers;

import com.skyfall.authmiddleware.security.dtos.LoginOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("login-options")
public class LoginOptionsController {
    @Autowired
    private OAuth2ClientProperties clientProperties;

    @GetMapping
    public Mono<List<LoginOption>> findAllLoginOptions(ServerHttpRequest request) {
        List<LoginOption> loginOptions = getLoginOptions(clientProperties, request);

        return Mono.just(loginOptions);
    }

    public List<LoginOption> getLoginOptions(
            OAuth2ClientProperties clientProperties,
            ServerHttpRequest request
    ) {
        String baseUrl = request.getURI().toString();
        baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(request.getURI().getPath()));

        URI clientUri = URI.create(baseUrl);

        final var clientAuthority = clientUri.getAuthority();

        return clientProperties.getRegistration()
                .entrySet()
                .stream()
                .filter(entrySet -> "authorization_code".equals(entrySet.getValue().getAuthorizationGrantType()))
                .map(entrySet -> {
                    final var label = entrySet.getValue().getProvider();
                    final var loginUri = "%s/oauth2/authorization/%s".formatted(clientUri, entrySet.getKey());
                    final var providerId = clientProperties.getRegistration().get(entrySet.getKey()).getProvider();
                    final var providerIssuerAuthority = URI.create(clientProperties.getProvider()
                                    .get(providerId)
                                    .getIssuerUri())
                            .getAuthority();

                    return new LoginOption(label, loginUri, Objects.equals(clientAuthority, providerIssuerAuthority));
                })
                .toList();
    }
}
