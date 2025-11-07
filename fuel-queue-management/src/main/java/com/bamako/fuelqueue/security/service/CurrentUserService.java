package com.bamako.fuelqueue.security.service;

import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.exception.UnauthorizedException;
import com.bamako.fuelqueue.security.model.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new UnauthorizedException("Authentication required.");
        }
        return principal.getUser();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
