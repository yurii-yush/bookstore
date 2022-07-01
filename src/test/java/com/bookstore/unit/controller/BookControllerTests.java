package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.BookController;
import com.bookstore.controller.dto.BookDTO;
import com.bookstore.controller.request.BookSearchRequest;
import com.bookstore.entity.Author;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.enums.BookGenre;
import com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.MapperFeature;
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

@WebMvcTest(BookController.class)
public class BookControllerTests {

    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO book;

    @BeforeEach
    public void setup() {
        List<Author> authors = List.of(new Author(1L, "Yura", "Yush", "UA", new ArrayList<>()));
        Publisher publisher = new Publisher(1L, "Home", "UA");
        book = new BookDTO("978-617-679-145-4", "Мистецтво війни", authors, publisher, BookGenre.DETECTIVE);
    }

    @Test
    public void givenValidBook_whenSaveBook_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(book);
        URI location = URI.create(String.format(Messages.CREATED_BOOK_URI, book.getIsbn()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        //when
        mockMvc.perform(
                        post(Messages.BOOK_CONTROLLER_URI)
                                .content(mapper.writeValueAsString(book))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(location)));
    }

    @Test
    public void givenValidSearchRequest_whenSearchBook_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenre(BookGenre.DETECTIVE);
        Page<BookDTO> expectedPage = new PageImpl<>(List.of(book), pagination, 1);

        Mockito.when(bookService.searchBook(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.BOOK_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenBookIsbn_whenDeleteBook_thenReturnStatus200() throws Exception {
        //given
        String isbn = book.getIsbn();

        //when
        mockMvc.perform(
                        delete(Messages.BOOK_CONTROLLER_URI + Messages.ISBN_MAPPING, isbn))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotValidIsbn_whenSaveBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setIsbn("5456-55");

        //when
        performSaveNotValidBook(book);
    }

    @Test
    public void givenNotValidTitle_whenSaveBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setTitle("dd");

        //when
        performSaveNotValidBook(book);
    }

    @Test
    public void givenNotValidAuthors_whenSaveBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setAuthor(null);

        //when
        performSaveNotValidBook(book);
    }

    @Test
    public void givenNotValidPublisher_whenSaveBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setPublisher(null);

        //when
        performSaveNotValidBook(book);
    }


    @Test
    public void givenNotValidTitle_whenUpdateBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setTitle("Y");

        //when
        performUpdateNotValidBook(book);
    }

    @Test
    public void givenNotValidIsbn_whenUpdateBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setIsbn("5654-5");

        //when
        performUpdateNotValidBook(book);
    }

    @Test
    public void givenNotValidAuthors_whenUpdateBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setAuthor(null);

        //when
        performUpdateNotValidBook(book);
    }

    @Test
    public void givenNotValidPublisher_whenUpdateBook_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        book.setPublisher(null);

        //when
        performUpdateNotValidBook(book);
    }

    @Test
    public void givenValidBook_whenUpdateBook_thenUpdatedBook_andStatus200() throws Exception {
        //given
        Mockito.when(bookService.updateBook(Mockito.any(), Mockito.any())).thenReturn(book);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);
        //when
        mockMvc.perform(
                        put(Messages.BOOK_CONTROLLER_URI + Messages.ISBN_MAPPING, book.getIsbn())
                                .content(mapper.writeValueAsString(book))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void performUpdateNotValidBook(BookDTO book) throws Exception {
        mockMvc.perform(
                        put(Messages.BOOK_CONTROLLER_URI + Messages.ISBN_MAPPING, book.getIsbn())
                                .content(objectMapper.writeValueAsString(book))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    private void performSaveNotValidBook(BookDTO book) throws Exception {
        mockMvc.perform(
                        post(Messages.BOOK_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(book))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
