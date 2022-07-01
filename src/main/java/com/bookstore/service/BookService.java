package com.bookstore.service;

import com.bookstore.controller.dto.BookDTO;
import com.bookstore.controller.request.BookSearchRequest;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<BookDTO> searchBook(BookSearchRequest searchRequest);

    BookDTO saveBook(BookDTO bookDTO);

    BookDTO updateBook(BookDTO bookDTO, String isbn);

    void deleteBookByIsbn(String isbn);
}
