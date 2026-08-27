package com.rohit.workflow_ai.client.controller;

import com.rohit.workflow_ai.client.dto.ClientResponse;
import com.rohit.workflow_ai.client.dto.CreateClientRequest;
import com.rohit.workflow_ai.client.dto.UpdateClientRequest;
import com.rohit.workflow_ai.client.service.ClientService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ==========================
    // Create Client
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateClientRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ClientResponse response =
                clientService.createClient(
                        request,
                        companyId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseUtil.created(
                                response,
                                "Client created successfully"
                        )
                );
    }

    // ==========================
    // Get All Clients
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponse>>> getAllClients(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        List<ClientResponse> response =
                clientService.getAllClients(companyId);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Clients fetched successfully"
                )
        );
    }

    // ==========================
    // Get Client By ID
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> getClientById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ClientResponse response =
                clientService.getClientById(
                        id,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Client fetched successfully"
                )
        );
    }

    // ==========================
    // Update Client
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id,
            @Valid @RequestBody UpdateClientRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ClientResponse response =
                clientService.updateClient(
                        id,
                        request,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Client updated successfully"
                )
        );
    }

    // ==========================
    // Delete Client
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        clientService.deleteClient(
                id,
                companyId
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        "Client deleted successfully"
                )
        );
    }
}