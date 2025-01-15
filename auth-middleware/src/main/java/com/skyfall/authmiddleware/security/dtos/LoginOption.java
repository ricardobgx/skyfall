package com.skyfall.authmiddleware.security.dtos;

import jakarta.validation.constraints.NotEmpty;

public record LoginOption(
        @NotEmpty
        String label,
        @NotEmpty
        String loginUri,
        boolean isSameAuthority
) {
}
