package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.ClientDTO;
import com.bookstore.controller.request.ClientSearchRequest;
import com.bookstore.entity.Client;
import com.bookstore.exception.EntityAlreadyExistException;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.ClientRepository;
import com.bookstore.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client1, client2, client3, client4;

    @BeforeEach
    public void setup() {
        client1 = new Client(1L, "Yura", "Yush", "+38(096)-37-44-555", "yura.tyt@gmail.com", "Ternopil, Bmyty 12/2");
        client2 = new Client(2L, "Sasha", "Kohyt", "+38(071)-75-26-404", "sasha.yush@gmail.com", "Ternopil, Terstylna 12/1");
        client3 = new Client(3L, "Slavik", "Sbarkev", "+38(063)-25-25-255", "slavik.stank@gmail.com", "Ternopil, Burbasa 4");
        client4 = new Client(4L, "Dima", "Desianchuk", "+38(073)-44-55-655", "yura.tyt@gmail.com", "Uzhorod, Tefi 4");
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchClient_thenReturnPageWithClients() {
        //given
        ClientSearchRequest searchRequest = new ClientSearchRequest("Yush", "+38(096)-37-44-555", "yura.tyt@gmail.com");

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<Client> pageFromDB = new PageImpl<>(List.of(client1, client2), page, 2L);
        Page<ClientDTO> expectedPage = getPageDTOList(new PageImpl<>(List.of(client1, client2), page, 2L));

        //when
        Page<ClientDTO> actualPage = doSearchClient(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchClient_thenReturnPageWithClients() {
        //given
        ClientSearchRequest searchRequest = new ClientSearchRequest();
        searchRequest.setSurname("Yus");
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<Client> pageFromDB = new PageImpl<>(List.of(client1, client4), page, 2L);
        Page<ClientDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<ClientDTO> actualPage = doSearchClient(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidClient_whenSaveClient_thenSaveAndReturnClient() throws Exception {
        //given
        when(clientRepository.saveAndFlush(Mockito.any()))
                .thenReturn(client1);

        //when
        ClientDTO actualClient = clientService.saveClient(client1.toDTO());

        //then
        assertThat(client1.toDTO(), equalTo(actualClient));
    }

    @Test
    public void givenNotValidEmail_whenSaveClient_thenThrowEntityAlreadyExistException() throws Exception {
        //given
        when(clientRepository.findByEmail(Mockito.any()))
                .thenReturn(Optional.of(client4));

        //then
        assertThrows(EntityAlreadyExistException.class, () -> clientService.saveClient(client1.toDTO()));//todo check message
    }

    @Test
    public void givenClientId_whenDeleteClient_thenDelete_andStatus200() throws Exception {
        //given
        long ClientId = 1L;

        //when
        clientService.deleteClientById(ClientId);

        //then
        verify(clientRepository, times(1)).deleteById(ClientId);
    }

    @Test
    public void givenValidClient_whenUpdateClient_thenUpdateAndReturnClient() {
        //given
        when(clientRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(client1));

        when(clientRepository.saveAndFlush(Mockito.any()))
                .thenReturn(client1);

        //when
        ClientDTO actualClient = clientService.updateClient(client1.toDTO(), client1.getId());

        //then
        assertThat(client1.toDTO(), equalTo(actualClient));
    }

    @Test
    public void givenNotValidClientId_whenUpdateClient_thenThrowEntityNotFoundException() {
        //when
        when(clientRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> clientService.updateClient(client1.toDTO(), client1.getId()));//todo check message
    }

    @Test
    public void givenNotValidEmail_whenUpdateClient_thenThrowEntityAlreadyExistException() throws Exception {
        //given
        when(clientRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(client1));

        when(clientRepository.findByEmail(Mockito.any()))
                .thenReturn(Optional.of(client4));

        //then
        assertThrows(EntityAlreadyExistException.class, () -> clientService.updateClient(client1.toDTO(), client1.getId()));
    }

    private Page<ClientDTO> doSearchClient(Page<Client> pageFromDB, ClientSearchRequest searchRequest) {
        when(clientRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return clientService.searchClient(searchRequest);
    }

    private Page<ClientDTO> getPageDTOList(Page<Client> Clients) {
        return new PageImpl<>(Clients.stream()
                .map(Client::toDTO)
                .collect(Collectors.toList()));
    }
}
