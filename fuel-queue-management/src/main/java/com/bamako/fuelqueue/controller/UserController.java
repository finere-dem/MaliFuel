package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.request.UserLocalityRegistrationRequest;
import com.bamako.fuelqueue.dto.request.UserUpdateRequest;
import com.bamako.fuelqueue.dto.response.UserResponse;
import com.bamako.fuelqueue.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(@Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @PostMapping("/me/localities")
    public ResponseEntity<UserResponse> addLocality(@Valid @RequestBody UserLocalityRegistrationRequest request) {
        return ResponseEntity.ok(userService.addLocalityToCurrentUser(request));
    }
}
