package com.rohit.workflow_ai.user.service;

import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.user.dto.UpdateProfileRequest;
import com.rohit.workflow_ai.user.dto.UserProfileResponse;
import com.rohit.workflow_ai.user.entity.User;
import com.rohit.workflow_ai.auth.mapper.UserMapper;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.toUserProfile(user);
    }
    public UserProfileResponse updateProfile(
            Authentication authentication,
            UpdateProfileRequest request) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        // Prevent duplicate email
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return UserMapper.toUserProfile(updatedUser);
    }
}