package com.skyfall.authmiddleware.user.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("me")
public class MeController {
    @GetMapping
    public Mono<OAuth2User> findMe(@AuthenticationPrincipal OAuth2User user) {
        return Mono.just(user);
    }
}
