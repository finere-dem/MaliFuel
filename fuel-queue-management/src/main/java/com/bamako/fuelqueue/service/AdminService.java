package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.user.UpdateUserRoleRequest;
import com.bamako.fuelqueue.dto.user.UserResponse;
import com.bamako.fuelqueue.entity.User;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.mapper.UserMapper;
import com.bamako.fuelqueue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUserRole(UUID userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
