package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.PublisherController;
import com.bookstore.controller.dto.PublisherDTO;
import com.bookstore.controller.request.PublisherSearchRequest;
import com.bookstore.service.PublisherService;
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

@WebMvcTest(PublisherController.class)
public class PublisherControllerTest {

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PublisherDTO publisher;

    @BeforeEach
    public void setup() {
        publisher = new PublisherDTO(1L, "Видавництво старого Лева", "UA");
    }

    @Test
    public void givenValidSearchRequest_whenSearchPublisher_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        PublisherSearchRequest searchRequest = new PublisherSearchRequest();
        searchRequest.setCountry("UA");
        Page<PublisherDTO> expectedPage = new PageImpl<>(List.of(publisher), pagination, 1);

        Mockito.when(publisherService.searchPublisher(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenNotValidCountryInSearchRequest_whenSearchPublisher_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        PublisherSearchRequest searchRequest = new PublisherSearchRequest();
        searchRequest.setCountry("UAR");

        //when
        mockMvc.perform(
                        get(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    @Test
    public void givenPublisherId_whenDeletePublisher_thenReturnStatus200() throws Exception {
        //given
        Long id = publisher.getId();

        //when
        mockMvc.perform(
                        delete(Messages.PUBLISHER_CONTROLLER_URI + Messages.ID_MAPPING, id))
                .andExpect(status().isOk());
    }

    @Test
    public void givenShortTitle_whenSavePublisher_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        publisher.setTitle("В");

        //when
        performSaveNotValidPublisher(publisher);
    }

    @Test
    public void givenNotValidCountry_whenSavePublisher_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        publisher.setCountry("UAG");

        //when
        performSaveNotValidPublisher(publisher);
    }

    @Test
    public void givenValidPublisher_whenSavePublisher_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(publisherService.savePublisher(Mockito.any())).thenReturn(publisher);
        URI location = URI.create(String.format(Messages.CREATED_PUBLISHER_URI, publisher.getId()));

        //when
        mockMvc.perform(
                        post(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(publisher))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));
    }

    @Test
    public void givenShortTitle_whenUpdatePublisher_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        publisher.setTitle("Y");

        //when
        performUpdateNotValidPublisher(publisher);
    }

    @Test
    public void givenNotValidCountry_whenUpdatePublisher_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        publisher.setCountry("UAE");

        //when
        performUpdateNotValidPublisher(publisher);
    }

    @Test
    public void givenValidPublisher_whenUpdatePublisher_thenUpdatedPublisher_andStatus200() throws Exception {
        //given
        Mockito.when(publisherService.updatePublisher(Mockito.any(), Mockito.any())).thenReturn(publisher);

        //when
        mockMvc.perform(
                        put(Messages.PUBLISHER_CONTROLLER_URI + Messages.ID_MAPPING, publisher.getId())
                                .content(objectMapper.writeValueAsString(publisher))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(publisher)));
    }

    private void performUpdateNotValidPublisher(PublisherDTO publisher) throws Exception {
        mockMvc.perform(
                        put(Messages.PUBLISHER_CONTROLLER_URI + Messages.ID_MAPPING, publisher.getId())
                                .content(objectMapper.writeValueAsString(publisher))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    private void performSaveNotValidPublisher(PublisherDTO publisher) throws Exception {
        mockMvc.perform(
                        post(Messages.PUBLISHER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(publisher))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
