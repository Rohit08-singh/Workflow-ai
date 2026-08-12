package com.rohit.workflow_ai.client.mapper;

import com.rohit.workflow_ai.client.dto.ClientResponse;
import com.rohit.workflow_ai.client.dto.CreateClientRequest;
import com.rohit.workflow_ai.client.entity.Client;
import org.bson.types.ObjectId;

public class ClientMapper {

    private ClientMapper() {
    }

    public static Client toEntity(CreateClientRequest request, ObjectId companyId) {

        return Client.builder()
                .companyId(companyId)
                .companyName(request.getCompanyName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    public static ClientResponse toResponse(Client client) {

        return ClientResponse.builder()
                .id(client.getId().toHexString())
                .companyName(client.getCompanyName())
                .contactPerson(client.getContactPerson())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .status(client.getStatus())
                .build();
    }
}