package com.rohit.workflow_ai.client.dto;

import com.rohit.workflow_ai.common.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponse {

    private String id;

    private String companyName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;

    private Status status;
}