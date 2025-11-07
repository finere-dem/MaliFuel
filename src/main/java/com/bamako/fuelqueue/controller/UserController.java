package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.domain.dto.user.AddUserLocalityRequest;
import com.bamako.fuelqueue.domain.dto.user.UpdateUserProfileRequest;
import com.bamako.fuelqueue.domain.dto.user.UserProfileDto;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMe() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateMe(@Valid @RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @PostMapping("/me/localities")
    public ResponseEntity<UserProfileDto> addLocality(@Valid @RequestBody AddUserLocalityRequest request) {
        return ResponseEntity.ok(userService.addLocalityToCurrentUser(request));
    }
}
