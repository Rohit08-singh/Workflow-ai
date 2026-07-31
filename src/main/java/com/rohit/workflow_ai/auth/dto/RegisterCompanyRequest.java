package com.rohit.workflow_ai.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterCompanyRequest {

    @NotBlank
    private String companyName;

    @Email
    private String companyEmail;

    private String companyPhone;

    private String companyWebsite;

    private String industry;

    @NotBlank
    private String adminFirstName;

    @NotBlank
    private String adminLastName;

    @Email
    private String adminEmail;

    @NotBlank
    private String adminPassword;

    private String adminPhone;

}