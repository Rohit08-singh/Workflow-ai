package com.rohit.workflow_ai.auth.mapper;

import com.rohit.workflow_ai.auth.dto.RegisterCompanyRequest;
import com.rohit.workflow_ai.auth.dto.UserResponse;
import com.rohit.workflow_ai.common.enums.UserRole;
import com.rohit.workflow_ai.company.entity.Company;
import com.rohit.workflow_ai.user.entity.User;
import org.bson.types.ObjectId;

public class UserMapper {

    private UserMapper() {
    }

    public static Company toCompany(RegisterCompanyRequest request) {

        return Company.builder()
                .companyName(request.getCompanyName())
                .email(request.getCompanyEmail())
                .phone(request.getCompanyPhone())
                .website(request.getCompanyWebsite())
                .industry(request.getIndustry())
                .build();
    }

    public static User toCompanyAdmin(RegisterCompanyRequest request,
                                      ObjectId companyId) {

        return User.builder()
                .companyId(companyId)
                .firstName(request.getAdminFirstName())
                .lastName(request.getAdminLastName())
                .email(request.getAdminEmail())
                .phone(request.getAdminPhone())
                .role(UserRole.COMPANY_ADMIN)
                .build();
    }

    public static UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId().toHexString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}