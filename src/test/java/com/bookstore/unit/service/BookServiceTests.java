package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.BookDTO;
import com.bookstore.controller.request.BookSearchRequest;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.enums.BookGenre;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.impl.BookServiceImpl;
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
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book1, book2, book3, book4;

    @BeforeEach
    public void setup() {
        List<Author> authors = List.of(new Author(1L, "Yura", "Yush", "UA", new ArrayList<>()));
        Publisher publisher = new Publisher(1L, "Home", "UA");
        book1 = new Book("978-617-679-145-4", "Мистецтво війни", publisher, authors, BookGenre.SCIENCE_FICTION);
        book2 = new Book("978-617-8024-01-7", "Інтернат", publisher, authors, BookGenre.HEALTH);
        book3 = new Book("978-617-7807-04-8", "Хлібне перемиря", publisher, authors, BookGenre.HORROR);
        book4 = new Book("978-966-97821-0-6", "Антена", publisher, authors, BookGenre.SCIENCE_FICTION);
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchBook_thenReturnPageWithBooks() {
        //given
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenre(BookGenre.SCIENCE_FICTION);

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<Book> pageFromDB = new PageImpl<>(List.of(book1, book4), page, 2L);
        Page<BookDTO> expectedPage = getPageDTOList(new PageImpl<>(List.of(book1, book4), page, 2L));

        //when
        Page<BookDTO> actualPage = doSearchBook(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchBook_thenReturnPageWithBooks() {
        //given
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setTitle("Мистецтво");
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<Book> pageFromDB = new PageImpl<>(List.of(book1), page, 2L);
        Page<BookDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<BookDTO> actualPage = doSearchBook(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidBook_whenSaveBook_thenSaveAndReturnBook() throws Exception {
        //given
        when(bookRepository.saveAndFlush(Mockito.any()))
                .thenReturn(book1);

        //when
        BookDTO actualBook = bookService.saveBook(book1.toDTO());

        //then
        assertThat(book1.toDTO(), equalTo(actualBook));
    }

    @Test
    public void givenBookIsbn_whenDeleteBook_thenDelete_andStatus200() throws Exception {
        //given
        String isbn = "978-617-679-145-4";

        //when
        bookService.deleteBookByIsbn(isbn);

        //then
        verify(bookRepository, times(1)).deleteById(isbn);
    }

    @Test
    public void givenValidBook_whenUpdateBook_thenUpdateAndReturnBook() {
        //given
        when(bookRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(book1));

        when(bookRepository.saveAndFlush(Mockito.any()))
                .thenReturn(book1);

        //when
        BookDTO actualBook = bookService.updateBook(book1.toDTO(), book1.getIsbn());

        //then
        assertThat(book1.toDTO(), equalTo(actualBook));
    }

    @Test
    public void givenNotValidBookIsbn_whenUpdateBook_thenThrowEntityNotFoundException() {
        //when
        when(bookRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(book1.toDTO(), book1.getIsbn()));//todo check message
    }

    private Page<BookDTO> doSearchBook(Page<Book> pageFromDB, BookSearchRequest searchRequest) {
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return bookService.searchBook(searchRequest);
    }

    private Page<BookDTO> getPageDTOList(Page<Book> Books) {
        return new PageImpl<>(Books.stream()
                .map(Book::toDTO)
                .collect(Collectors.toList()));
    }
}
