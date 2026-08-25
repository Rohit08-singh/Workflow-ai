package com.rohit.workflow_ai.user.service;

import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.user.dto.*;
import com.rohit.workflow_ai.user.entity.User;
import com.rohit.workflow_ai.user.mapper.UserMapper;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================
    // Get Logged In User
    // ==========================
    public UserProfileResponse getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        return UserMapper.toUserProfile(user);
    }

    // ==========================
    // Update Profile
    // ==========================
    public UserProfileResponse updateProfile(
            Authentication authentication,
            UpdateProfileRequest request) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

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

    // ==========================
    // Create Employee
    // ==========================
    public UserResponse createUser(
            CreateUserRequest request,
            ObjectId companyId) {

        if (userRepository.existsByEmailAndCompanyId(
                request.getEmail(),
                companyId)) {

            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = UserMapper.toEntity(request, companyId);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    // ==========================
    // Get All Employees
    // ==========================
    public List<UserResponse> getAllUsers(ObjectId companyId) {

        return userRepository.findByCompanyId(companyId)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // ==========================
// Get Employee By Id
// ==========================
    public UserResponse getUserById(
            String userId,
            ObjectId companyId) {

        User user = userRepository
                .findByIdAndCompanyId(
                        new ObjectId(userId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.USER_NOT_FOUND
                        ));

        return UserMapper.toResponse(user);
    }

    // ==========================
// Update Employee
// ==========================
    public UserResponse updateUser(
            String userId,
            UpdateUserRequest request,
            ObjectId companyId) {

        User user = userRepository
                .findByIdAndCompanyId(
                        new ObjectId(userId),
                        companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    public void deleteUser(String userId, ObjectId companyId) {

        User user = userRepository
                .findByIdAndCompanyId(
                        new ObjectId(userId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(Status.DELETED);

        userRepository.save(user);
    }
}