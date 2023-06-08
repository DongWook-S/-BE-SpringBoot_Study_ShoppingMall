package com.example.uk.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements org.springframework.data.domain.AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {

        String userId = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }
}
