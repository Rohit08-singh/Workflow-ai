package com.rohit.workflow_ai.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateClientRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Contact person is required")
    private String contactPerson;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private String address;
}