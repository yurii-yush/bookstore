package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.AuthorController;
import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.controller.request.AuthorSearchRequest;
import com.bookstore.service.AuthorService;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorDTO author;

    @BeforeEach
    public void setup() {
        author = new AuthorDTO(1L, "Yura", "Yushchyshyn", "UA", new ArrayList<>());
    }

    @Test
    public void givenValidSearchRequest_whenSearchAuthor_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        AuthorSearchRequest searchRequest = new AuthorSearchRequest();
        searchRequest.setCountry("UA");
        Page<AuthorDTO> expectedPage = new PageImpl<>(List.of(author), pagination, 1);

        Mockito.when(authorService.searchAuthor(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenNotValidCountryInSearchRequest_whenSearchAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        AuthorSearchRequest searchRequest = new AuthorSearchRequest();
        searchRequest.setCountry("UAR");

        //when
        mockMvc.perform(
                        get(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    @Test
    public void givenAuthorId_whenDeleteAuthor_thenReturnStatus200() throws Exception {
        //given
        Long id = author.getId();

        //when
        mockMvc.perform(
                        delete(Messages.AUTHOR_CONTROLLER_URI + Messages.ID_MAPPING, id))
                .andExpect(status().isOk());
    }

    @Test
    public void givenShortName_whenSaveAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setName("Yu");

        //when
        performSaveNotValidAuthor(author);
    }

    @Test
    public void givenShortSurname_whenSaveAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setSurname("");

        //when
        performSaveNotValidAuthor(author);
    }

    @Test
    public void givenNotValidCountry_whenSaveAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setCountry("UAG");

        //when
        performSaveNotValidAuthor(author);
    }

    @Test
    public void givenValidAuthor_whenSaveAuthor_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(authorService.saveAuthor(Mockito.any())).thenReturn(author);
        URI location = URI.create(String.format(Messages.CREATED_AUTHOR_URI, author.getId()));

        //when
        mockMvc.perform(
                        post(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(author))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));
    }

    @Test
    public void givenShortName_whenUpdateAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setName("Y");

        //when
        performUpdateNotValidAuthor(author);
    }

    @Test
    public void givenShortSurname_whenUpdateAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setSurname("Yu");

        //when
        performUpdateNotValidAuthor(author);
    }

    @Test
    public void givenNotValidCountry_whenUpdateAuthor_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        author.setCountry("UAE");

        //when
        performUpdateNotValidAuthor(author);
    }

    @Test
    public void givenValidAuthor_whenUpdateAuthor_thenUpdatedAuthor_andStatus200() throws Exception {
        //given
        Mockito.when(authorService.updateAuthor(Mockito.any(), Mockito.any())).thenReturn(author);

        //when
        mockMvc.perform(
                        put(Messages.AUTHOR_CONTROLLER_URI + Messages.ID_MAPPING, author.getId())
                                .content(objectMapper.writeValueAsString(author))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));
    }

    private void performUpdateNotValidAuthor(AuthorDTO author) throws Exception {
        mockMvc.perform(
                        put(Messages.AUTHOR_CONTROLLER_URI + Messages.ID_MAPPING, author.getId())
                                .content(objectMapper.writeValueAsString(author))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    private void performSaveNotValidAuthor(AuthorDTO author) throws Exception {
        mockMvc.perform(
                        post(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(author))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}