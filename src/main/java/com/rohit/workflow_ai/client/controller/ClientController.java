package com.rohit.workflow_ai.client.controller;

import com.rohit.workflow_ai.client.dto.ClientResponse;
import com.rohit.workflow_ai.client.dto.CreateClientRequest;
import com.rohit.workflow_ai.client.dto.UpdateClientRequest;
import com.rohit.workflow_ai.client.service.ClientService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.user.entity.User;
import com.rohit.workflow_ai.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import com.rohit.workflow_ai.user.entity.User;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final UserRepository userRepository;

    public ClientController(ClientService clientService,
                            UserRepository userRepository) {
        this.clientService = clientService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(
            @Valid @RequestBody CreateClientRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        ClientResponse response = clientService.createClient(
                request,
                user.getCompanyId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseUtil.created(
                        response,
                        "Client created successfully"
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponse>>> getAllClients(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        List<ClientResponse> response =
                clientService.getAllClients(user.getCompanyId());

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Clients fetched successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> getClientById(
            @PathVariable String id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        ClientResponse response = clientService.getClientById(
                id,
                user.getCompanyId()
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Client fetched successfully"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(
            @PathVariable String id,
            @Valid @RequestBody UpdateClientRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        ClientResponse response = clientService.updateClient(
                id,
                request,
                user.getCompanyId()
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Client updated successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable String id,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        clientService.deleteClient(
                id,
                user.getCompanyId()
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        "Client deleted successfully"
                )
        );
    }
}