package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.ClientController;
import com.bookstore.controller.dto.ClientDTO;
import com.bookstore.controller.request.ClientSearchRequest;
import com.bookstore.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientDTO client;

    @BeforeEach
    public void setup() {
        client = new ClientDTO(1L, "Yura", "Yush", "+38(067)-55-55-555", "yura.yush@gmail.com", "Ternopil, vyl Tekstylna 12");
    }

    @Test
    public void givenValidSearchRequest_whenSearchClient_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        ClientSearchRequest searchRequest = new ClientSearchRequest();
        searchRequest.setEmail("yura.yush@gmail.com");
        Page<ClientDTO> expectedPage = new PageImpl<>(List.of(client), pagination, 1);

        Mockito.when(clientService.searchClient(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.CLIENT_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenNotValidEmailInSearchRequest_whenSearchClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        ClientSearchRequest searchRequest = new ClientSearchRequest();
        searchRequest.setEmail("fd@ff.j");

        //when
        mockMvc.perform(
                        get(Messages.CLIENT_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    @Test
    public void givenClientId_whenDeleteClient_thenReturnStatus200() throws Exception {
        //given
        Long id = client.getId();

        //when
        mockMvc.perform(
                        delete(Messages.CLIENT_CONTROLLER_URI + Messages.ID_MAPPING, id))
                .andExpect(status().isOk());
    }

    @Test
    public void givenShortName_whenSaveClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setName("Yu");

        //when
        performSaveNotValidClient(client);
    }

    @Test
    public void givenShortSurname_whenSaveClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setSurname("");

        //when
        performSaveNotValidClient(client);
    }

    @Test
    public void givenNotValidEmail_whenSaveClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setEmail("fggf");

        //when
        performSaveNotValidClient(client);
    }

    @Test
    public void givenNotValidPhoneNumber_whenSaveClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setPhoneNumber("+3806578532");

        //when
        performSaveNotValidClient(client);
    }

    @Test
    public void givenNotValidAddress_whenSaveClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setAddress("Tern");

        //when
        performSaveNotValidClient(client);
    }

    @Test
    public void givenValidClient_whenSaveClient_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(clientService.saveClient(Mockito.any())).thenReturn(client);
        URI location = URI.create(String.format(Messages.CREATED_CLIENT_URI, client.getId()));

        //when
        mockMvc.perform(
                        post(Messages.CLIENT_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(client))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));
    }

    @Test
    public void givenShortName_whenUpdateClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setName("Y");

        //when
        performUpdateNotValidClient(client);
    }

    @Test
    public void givenShortSurname_whenUpdateClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setSurname("Yu");

        //when
        performUpdateNotValidClient(client);
    }

    @Test
    public void givenNotValidEmail_whenUpdateClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setEmail("fgfg");

        //when
        performUpdateNotValidClient(client);
    }

    @Test
    public void givenNotValidPhoneNumber_whenUpdateClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setPhoneNumber("+3586525");

        //when
        performUpdateNotValidClient(client);
    }

    @Test
    public void givenNotValidAddress_whenUpdateClient_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        client.setAddress("Ternop");

        //when
        performUpdateNotValidClient(client);
    }

    @Test
    public void givenValidClient_whenUpdateClient_thenUpdatedClient_andStatus200() throws Exception {
        //given
        Mockito.when(clientService.updateClient(Mockito.any(), Mockito.any())).thenReturn(client);

        //when
        mockMvc.perform(
                        put(Messages.CLIENT_CONTROLLER_URI + Messages.ID_MAPPING, client.getId())
                                .content(objectMapper.writeValueAsString(client))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(client)));
    }

    private void performUpdateNotValidClient(ClientDTO Client) throws Exception {
        mockMvc.perform(
                        put(Messages.CLIENT_CONTROLLER_URI + Messages.ID_MAPPING, Client.getId())
                                .content(objectMapper.writeValueAsString(Client))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    private void performSaveNotValidClient(ClientDTO Client) throws Exception {
        mockMvc.perform(
                        post(Messages.CLIENT_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(Client))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
