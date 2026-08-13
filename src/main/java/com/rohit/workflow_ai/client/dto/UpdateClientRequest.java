package com.rohit.workflow_ai.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateClientRequest {

    @NotBlank
    private String companyName;

    @NotBlank
    private String contactPerson;

    @Email
    @NotBlank
    private String email;

    private String phone;

    private String address;
}