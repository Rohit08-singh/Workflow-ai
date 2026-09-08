package com.rohit.workflow_ai.auth.service;

import com.rohit.workflow_ai.auth.dto.*;
import com.rohit.workflow_ai.auth.entity.PasswordResetToken;
import com.rohit.workflow_ai.auth.mapper.UserMapper;
import com.rohit.workflow_ai.auth.repository.PasswordResetTokenRepository;
import com.rohit.workflow_ai.company.entity.Company;
import com.rohit.workflow_ai.company.repository.CompanyRepository;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.security.jwt.JwtService;
import com.rohit.workflow_ai.user.entity.User;
import com.rohit.workflow_ai.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Password reset dependencies
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;

    // Frontend URL
    @Value("${app.frontend-url}")
    private String frontendUrl;


    public AuthService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordResetTokenService passwordResetTokenService,
            EmailService emailService
    ) {

        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
    }


    // =========================================================
    // REGISTER COMPANY
    // =========================================================

    public UserResponse registerCompany(RegisterCompanyRequest request) {

        if (companyRepository.existsByCompanyName(
                request.getCompanyName())) {

            throw new AppException(
                    ErrorCode.COMPANY_ALREADY_EXISTS
            );
        }

        if (userRepository.existsByEmail(
                request.getAdminEmail())) {

            throw new AppException(
                    ErrorCode.EMAIL_ALREADY_EXISTS
            );
        }

        Company company =
                UserMapper.toCompany(request);

        Company savedCompany =
                companyRepository.save(company);

        User admin =
                UserMapper.toCompanyAdmin(
                        request,
                        savedCompany.getId()
                );

        admin.setPassword(
                passwordEncoder.encode(
                        request.getAdminPassword()
                )
        );

        User savedUser =
                userRepository.save(admin);

        return UserMapper.toUserResponse(savedUser);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse login(LoginRequest request) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(() ->
                        new AppException(
                                ErrorCode.INVALID_CREDENTIALS
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new AppException(
                    ErrorCode.INVALID_CREDENTIALS
            );
        }

        String accessToken =
                jwtService.generateToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserMapper.toUserResponse(user))
                .build();
    }


    // =========================================================
    // REFRESH TOKEN
    // =========================================================

    public LoginResponse refreshToken(
            RefreshTokenRequest request) {

        if (!jwtService.validateToken(
                request.getRefreshToken()
        )) {

            throw new AppException(
                    ErrorCode.INVALID_REFRESH_TOKEN
            );
        }

        String email =
                jwtService.extractEmail(
                        request.getRefreshToken()
                );

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new AppException(
                                        ErrorCode.USER_NOT_FOUND
                                )
                        );

        String accessToken =
                jwtService.generateToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserMapper.toUserResponse(user))
                .build();
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    public void logout() {

        // Stateless JWT logout.
        // Frontend removes access and refresh tokens.
    }


    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    public ForgotPasswordResponse forgotPassword(
            ForgotPasswordRequest request) {

        // 1. Find user
        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(() ->
                        new AppException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );


        // 2. Delete any previous reset tokens
        passwordResetTokenRepository.deleteByUserId(
                user.getId()
        );


        // 3. Generate secure random token
        String rawToken =
                passwordResetTokenService.generateToken();


        // 4. Hash token before storing it
        String tokenHash =
                passwordResetTokenService.hashToken(
                        rawToken
                );


        // 5. Current time
        LocalDateTime now =
                LocalDateTime.now();


        // 6. Create password reset token document
        PasswordResetToken resetToken =
                PasswordResetToken.builder()
                        .userId(user.getId())
                        .tokenHash(tokenHash)
                        .expiresAt(
                                now.plusMinutes(15)
                        )
                        .used(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();


        // 7. Save token in MongoDB
        passwordResetTokenRepository.save(
                resetToken
        );


        // 8. Create frontend reset URL
        String resetLink =
                frontendUrl
                        + "/reset-password?token="
                        + rawToken;


        // 9. Send reset email
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetLink
        );


        // 10. Don't return the token
        return ForgotPasswordResponse.builder()
                .message(
                        "Password reset link sent successfully"
                )
                .build();
    }


    // =========================================================
    // RESET PASSWORD
    // =========================================================

    public void resetPassword(
            ResetPasswordRequest request) {

        // 1. Hash the token received from frontend
        String tokenHash =
                passwordResetTokenService.hashToken(
                        request.getResetToken()
                );


        // 2. Find unused token
        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHashAndUsedFalse(
                                tokenHash
                        )
                        .orElseThrow(() ->
                                new AppException(
                                        ErrorCode.INVALID_RESET_TOKEN
                                )
                        );


        // 3. Check token expiration
        if (resetToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new AppException(
                    ErrorCode.INVALID_RESET_TOKEN
            );
        }


        // 4. Find user
        User user =
                userRepository.findById(
                        resetToken.getUserId()
                ).orElseThrow(() ->
                        new AppException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );


        // 5. Encode new password using BCrypt
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        // 6. Save updated user
        userRepository.save(user);


        // 7. Mark reset token as used
        resetToken.setUsed(true);

        resetToken.setUpdatedAt(
                LocalDateTime.now()
        );


        // 8. Save updated reset token
        passwordResetTokenRepository.save(
                resetToken
        );
    }
}