package com.bookstore.service;

import com.bookstore.controller.dto.ClientDTO;
import com.bookstore.controller.request.ClientSearchRequest;
import org.springframework.data.domain.Page;

public interface ClientService {
    Page<ClientDTO> searchClient(ClientSearchRequest searchRequest);

    ClientDTO updateClient(ClientDTO clientDTO, Long id);

    ClientDTO saveClient(ClientDTO clientDTO);

    void deleteClientById(Long id);
}
