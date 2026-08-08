package com.rohit.workflow_ai.auth.service;

import com.rohit.workflow_ai.auth.dto.*;
import com.rohit.workflow_ai.company.repository.CompanyRepository;
import com.rohit.workflow_ai.security.jwt.JwtService;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rohit.workflow_ai.auth.mapper.UserMapper;
import com.rohit.workflow_ai.company.entity.Company;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.user.entity.User;


@Service
public class AuthService {


    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public UserResponse registerCompany(RegisterCompanyRequest request) {

        if (companyRepository.existsByCompanyName(request.getCompanyName())) {
            throw new AppException(ErrorCode.COMPANY_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Company company = UserMapper.toCompany(request);

        Company savedCompany = companyRepository.save(company);

        User admin = UserMapper.toCompanyAdmin(
                request,
                savedCompany.getId()
        );

        admin.setPassword(
                passwordEncoder.encode(request.getAdminPassword())
        );

        User savedUser = userRepository.save(admin);

        return UserMapper.toUserResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserMapper.toUserResponse(user))
                .build();
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {

        if (!jwtService.validateToken(request.getRefreshToken())) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtService.extractEmail(request.getRefreshToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND)
                );

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserMapper.toUserResponse(user))
                .build();
    }

}