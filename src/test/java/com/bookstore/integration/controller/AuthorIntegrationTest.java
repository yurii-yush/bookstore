package com.bookstore.integration.controller;

import com.bookstore.common.Messages;
import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.controller.request.AuthorSearchRequest;
import com.bookstore.entity.Author;
import com.bookstore.integration.AbstractIT;
import com.bookstore.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorIntegrationTest extends AbstractIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenValidAuthor_whenSaveAuthor_thenReturnURI_andStatus201() throws Exception {
        //given
        long maxId = getMaxId() + 1;
        Author expectedAuthor = new Author(maxId, "Сунь", "Цзи", "CN", new ArrayList<>());
        URI location = URI.create(String.format(Messages.CREATED_AUTHOR_URI, maxId));

        //when
        mockMvc.perform(
                        post(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(expectedAuthor))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));

        //then
        Optional<Author> actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(expectedAuthor, equalTo(actualAuthor.get()));
    }

    @Test
    public void givenAuthorId_whenDeleteAuthor_thenDeleteAuthor_andStatus200() throws Exception {
        //given
        addAuthors();
        Author expectedAuthor = getSingleAuthor();

        //when
        mockMvc.perform(
                        delete(Messages.AUTHOR_CONTROLLER_URI + Messages.ID_MAPPING, expectedAuthor.getId()))
                .andExpect(status().isOk());

        //then
        Optional<Author> actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(actualAuthor, equalTo(Optional.empty()));
    }

    @Test
    public void givenSurname_whenSearchAuthor_thenReturnListOfAuthors_andStatus200() throws Exception {
        //given
        String surname = "Ремарк";
        AuthorSearchRequest searchRequest = new AuthorSearchRequest();
        searchRequest.setSurname(surname);

        //when
        List<Author> allAuthors = addAuthors();
        List<AuthorDTO> expectedAuthors = filterAuthorsBySurname(allAuthors, surname);

        //then
        performAuthorSearch(searchRequest, expectedAuthors);
    }

    @Test
    public void givenValidCountry_whenSearchAuthor_thenReturnListOfAuthors_andStatus200() throws Exception {
        //given
        String country = "UA";
        AuthorSearchRequest searchRequest = new AuthorSearchRequest();
        searchRequest.setCountry(country);

        //when
        List<Author> allAuthors = addAuthors();
        List<AuthorDTO> expectedAuthors = filterAuthorsByCountry(allAuthors, country);

        //then
        performAuthorSearch(searchRequest, expectedAuthors);
    }

    @Test
    public void givenValidAuthor_whenUpdateAuthor_thenReturnURI_andStatus200() throws Exception {
        //given
        addAuthors();
        Author expectedAuthor = getSingleAuthor();
        expectedAuthor.setSurname("Borysenko");

        //when
        mockMvc.perform(
                        put(Messages.AUTHOR_CONTROLLER_URI + Messages.ID_MAPPING, expectedAuthor.getId())
                                .content(objectMapper.writeValueAsString(expectedAuthor.toDTO()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor.toDTO())));

        //then
        Optional<Author> actualAuthor = authorRepository.findById(expectedAuthor.getId());
        assertThat(expectedAuthor, equalTo(actualAuthor.get()));
    }

    private void performAuthorSearch(AuthorSearchRequest searchRequest, List<AuthorDTO> expectedAuthors) throws Exception {
        //given
        Page<AuthorDTO> expectedPage = new PageImpl<>(expectedAuthors);

        //when
        mockMvc.perform(
                        get(Messages.AUTHOR_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    private List<AuthorDTO> filterAuthorsByCountry(List<Author> allAuthors, String country) {
        return allAuthors.stream()
                .filter(author -> author.getCountry().contains(country))
                .map(Author::toDTO)
                .collect(Collectors.toList());
    }

    private List<AuthorDTO> filterAuthorsBySurname(List<Author> allAuthors, String surname) {
        return allAuthors.stream()
                .filter(author -> author.getSurname().contains(surname))
                .map(Author::toDTO)
                .collect(Collectors.toList());
    }

    private List<Author> addAuthors() {
        Author author1 = new Author(1L, "Сунь", "Цзи", "CN", new ArrayList<>());
        Author author2 = new Author(2L, "Сергій", "Жадан", "UA", new ArrayList<>());
        Author author3 = new Author(3L, "Олександр", "Авраменко", "UA", new ArrayList<>());
        Author author4 = new Author(4L, "Олдос", "Хакслі", "GB", new ArrayList<>());
        Author author5 = new Author(5L, "Меган", "МакДоналд", "US", new ArrayList<>());
        Author author6 = new Author(6L, "Еpix Марія", "Ремарк", "UA", new ArrayList<>());
        Author author7 = new Author(7L, "Деніел", "Кіз", "US", new ArrayList<>());
        List<Author> authors = List.of(author1, author2, author3, author4, author5, author6, author7);
        authorRepository.deleteAll();

        return authorRepository.saveAllAndFlush(authors);
    }

    private long getMaxId() {
        Author previousAuthor = new Author(1L, "Сунь", "Цзи", "CN", new ArrayList<>());

        return authorRepository.saveAndFlush(previousAuthor).getId();

    }

    private Author getSingleAuthor() {
        return authorRepository.findAll().get(0);
    }
}
