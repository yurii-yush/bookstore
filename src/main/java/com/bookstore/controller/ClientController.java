package com.bookstore.controller;

import com.bookstore.common.Messages;
import com.bookstore.controller.dto.ClientDTO;
import com.bookstore.controller.request.ClientSearchRequest;
import com.bookstore.service.ClientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = Messages.CLIENT_CONTROLLER_URI)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "This method is used to search Clients")
    @GetMapping
    public ResponseEntity<Page<ClientDTO>> searchClient(@Valid @RequestBody ClientSearchRequest searchRequest) {
        Page<ClientDTO> clients = clientService.searchClient(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @ApiOperation(value = "This method is used to save new Client")
    @PostMapping
    public ResponseEntity<URI> saveClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO createdClient = clientService.saveClient(clientDTO);
        URI location = URI.create(String.format(Messages.CREATED_CLIENT_URI, createdClient.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to delete Client by ID")
    @DeleteMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<?> deleteClientById(@PathVariable(Messages.ISBN_PATH) Long id) {
        clientService.deleteClientById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "This method is used to update Client by ID")
    @PutMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<ClientDTO> updateClient(@RequestBody
                                                  @Valid ClientDTO clientDTO,
                                                  @PathVariable(Messages.ID_PATH) Long id) {
        ClientDTO updatedClientDTO = clientService.updateClient(clientDTO, id);

        return ResponseEntity.status(HttpStatus.OK).body(updatedClientDTO);
    }
}
