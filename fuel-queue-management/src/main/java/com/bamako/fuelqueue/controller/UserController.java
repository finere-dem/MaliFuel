package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.user.AddLocalityRequest;
import com.bamako.fuelqueue.dto.user.UpdateUserRequest;
import com.bamako.fuelqueue.dto.user.UserResponse;
import com.bamako.fuelqueue.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @PostMapping("/me/localities")
    public ResponseEntity<UserResponse> addLocality(@Valid @RequestBody AddLocalityRequest request) {
        return ResponseEntity.ok(userService.addLocality(request));
    }
}
