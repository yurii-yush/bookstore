package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.controller.request.AuthorSearchRequest;
import com.bookstore.entity.Author;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.impl.AuthorServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author1, author2, author3, author4;

    @BeforeEach
    public void setup() {
        author1 = new Author(1L, "Yura", "Yushchyshyn", "UA", new ArrayList<>());
        author2 = new Author(2L, "Vova", "Bushenko", "UA", new ArrayList<>());
        author3 = new Author(3L, "Serhii", "Kyrchenko", "PL", new ArrayList<>());
        author4 = new Author(4L, "Vova", "Yushchyshyn", "UK", new ArrayList<>());
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchAuthor_thenReturnPageWithAuthors() {
        //given
        AuthorSearchRequest searchRequest = new AuthorSearchRequest(null, "UA");

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<Author> pageFromDB = new PageImpl<>(List.of(author1, author2), page, 2L);
        Page<AuthorDTO> expectedPage = getPageDTOList(new PageImpl<>(List.of(author1, author2), page, 2L));

        //when
        Page<AuthorDTO> actualPage = doSearchAuthor(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchAuthor_thenReturnPageWithAuthors() {
        //given
        AuthorSearchRequest searchRequest = new AuthorSearchRequest("shchyshyn", null);
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<Author> pageFromDB = new PageImpl<>(List.of(author1, author4), page, 2L);
        Page<AuthorDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<AuthorDTO> actualPage = doSearchAuthor(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidAuthor_whenSaveAuthor_thenSaveAndReturnAuthor() throws Exception {
        //given
        when(authorRepository.saveAndFlush(Mockito.any()))
                .thenReturn(author1);

        //when
        AuthorDTO actualAuthor = authorService.saveAuthor(author1.toDTO());

        //then
        assertThat(author1.toDTO(), equalTo(actualAuthor));
    }

    @Test
    public void givenAuthorId_whenDeleteAuthor_thenDelete_andStatus200() throws Exception {
        //given
        long authorId = 1L;

        //when
        authorService.deleteAuthorById(authorId);

        //then
        verify(authorRepository, times(1)).deleteById(authorId);
    }

    @Test
    public void givenValidAuthor_whenUpdateAuthor_thenUpdateAndReturnAuthor() {
        //given
        when(authorRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(author1));

        when(authorRepository.saveAndFlush(Mockito.any()))
                .thenReturn(author1);

        //when
        AuthorDTO actualAuthor = authorService.updateAuthor(author1.toDTO(), author1.getId());

        //then
        assertThat(author1.toDTO(), equalTo(actualAuthor));
    }

    @Test
    public void givenNotValidAuthorId_whenUpdateAuthor_thenThrowEntityNotFoundException() {
        //when
        when(authorRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> authorService.updateAuthor(author1.toDTO(), author1.getId()));//todo check message
    }

    private Page<AuthorDTO> doSearchAuthor(Page<Author> pageFromDB, AuthorSearchRequest searchRequest) {
        when(authorRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return authorService.searchAuthor(searchRequest);
    }

    private Page<AuthorDTO> getPageDTOList(Page<Author> authors) {
        return new PageImpl<>(authors.stream()
                .map(Author::toDTO)
                .collect(Collectors.toList()));
    }
}
