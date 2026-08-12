package com.rohit.workflow_ai.client.service;

import com.rohit.workflow_ai.client.dto.ClientResponse;
import com.rohit.workflow_ai.client.dto.CreateClientRequest;
import com.rohit.workflow_ai.client.dto.UpdateClientRequest;
import com.rohit.workflow_ai.client.entity.Client;
import com.rohit.workflow_ai.client.mapper.ClientMapper;
import com.rohit.workflow_ai.client.repository.ClientRepository;
import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse createClient(CreateClientRequest request, ObjectId companyId) {

        if (clientRepository.existsByEmailAndCompanyId(
                request.getEmail(),
                companyId
        )) {
            throw new AppException(ErrorCode.CLIENT_ALREADY_EXISTS);
        }

        Client client = ClientMapper.toEntity(request, companyId);

        Client savedClient = clientRepository.save(client);

        return ClientMapper.toResponse(savedClient);
    }

    public List<ClientResponse> getAllClients(ObjectId companyId) {

        List<Client> clients = clientRepository.findByCompanyId(companyId);

        return clients.stream()
                .map(ClientMapper::toResponse)
                .toList();
    }

    public ClientResponse getClientById(String clientId, ObjectId companyId) {

        Client client = clientRepository
                .findByIdAndCompanyId(new ObjectId(clientId), companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CLIENT_NOT_FOUND)
                );

        return ClientMapper.toResponse(client);
    }


    public ClientResponse updateClient(String clientId,
                                       UpdateClientRequest request,
                                       ObjectId companyId) {

        Client client = clientRepository
                .findByIdAndCompanyId(new ObjectId(clientId), companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CLIENT_NOT_FOUND));

        client.setCompanyName(request.getCompanyName());
        client.setContactPerson(request.getContactPerson());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        Client updatedClient = clientRepository.save(client);

        return ClientMapper.toResponse(updatedClient);
    }

    public void deleteClient(String clientId, ObjectId companyId) {

        Client client = clientRepository
                .findByIdAndCompanyId(new ObjectId(clientId), companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CLIENT_NOT_FOUND));

        client.setStatus(Status.DELETED);

        clientRepository.save(client);
    }
}