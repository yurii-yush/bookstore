package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.ClientDTO;
import com.bookstore.entity.Client;
import com.bookstore.exception.EntityAlreadyExistException;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.ClientRepository;
import com.bookstore.repository.specification.ClientSpecifications;
import com.bookstore.controller.request.ClientSearchRequest;
import com.bookstore.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService, Pagination {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ClientDTO> searchClient(ClientSearchRequest searchRequest) {
        Specification<Client> query = ClientSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<Client> clients = clientRepository.findAll(query, page);

        return getClientDTOList(clients);
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO, Long clientId) {
        if (!isClientIdValid(clientId)) {
            throw new EntityNotFoundException(String.format(Messages.CLIENT_ID_NOT_FOUND, clientId));
        }

        clientDTO.setId(clientId);
        return saveClient(clientDTO);
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientDTO) {
        if (!isEmailValid(clientDTO.getEmail(), clientDTO.getId())) {
            throw new EntityAlreadyExistException(String.format(Messages.CLIENT_ALREADY_EXISTS, clientDTO.getEmail()));
        }
        Client savedClient = clientRepository.saveAndFlush(clientDTO.toEntity());

        return savedClient.toDTO();
    }

    @Override
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    private Page<ClientDTO> getClientDTOList(Page<Client> clients) {
        return new PageImpl<>(clients.stream()
                .map(Client::toDTO)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private boolean isClientIdValid(Long id) {
        return clientRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = true)
    private boolean isEmailValid(String email, Long clientId) {
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isEmpty()) {
            return true;
        }
        return client.get().getId().equals(clientId);
    }
}
