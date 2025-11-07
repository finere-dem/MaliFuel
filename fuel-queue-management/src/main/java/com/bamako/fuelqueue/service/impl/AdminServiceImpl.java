package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.enumeration.UserRole;
import com.bamako.fuelqueue.domain.mapper.UserMapper;
import com.bamako.fuelqueue.domain.repository.UserRepository;
import com.bamako.fuelqueue.dto.response.UserResponse;
import com.bamako.fuelqueue.exception.ResourceNotFoundException;
import com.bamako.fuelqueue.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        return userMapper.toDto(resolveUser(userId));
    }

    @Override
    public UserResponse updateUserRole(UUID userId, UserRole role) {
        User user = resolveUser(userId);
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = resolveUser(userId);
        userRepository.delete(user);
    }

    private User resolveUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }
}
