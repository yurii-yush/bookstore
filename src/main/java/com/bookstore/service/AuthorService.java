package com.bookstore.service;

import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.controller.request.AuthorSearchRequest;
import org.springframework.data.domain.Page;

public interface AuthorService {
    Page<AuthorDTO> searchAuthor(AuthorSearchRequest searchRequest);

    AuthorDTO updateAuthor(AuthorDTO author, Long id);

    AuthorDTO saveAuthor(AuthorDTO author);

    void deleteAuthorById(Long id);
}
