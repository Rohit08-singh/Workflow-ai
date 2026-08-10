package com.rohit.workflow_ai.user.service;

import com.rohit.workflow_ai.auth.dto.UserResponse;
import com.rohit.workflow_ai.auth.mapper.UserMapper;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.user.entity.User;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.toUserResponse(user);
    }

}