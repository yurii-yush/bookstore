package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.BookDTO;
import com.bookstore.entity.Book;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.specification.BookSpecifications;
import com.bookstore.controller.request.BookSearchRequest;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService, Pagination {

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> searchBook(BookSearchRequest searchRequest) {
        Specification<Book> query = BookSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<Book> books = bookRepository.findAll(query, page);

        return getBookDTOList(books);
    }

    @Override
    public BookDTO saveBook(BookDTO book) {
        Book savedAuthor = bookRepository.saveAndFlush(book.toEntity());

        return savedAuthor.toDTO();
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, String isbn) {
        if (!isBookValid(isbn)) {
            throw new EntityNotFoundException(String.format(Messages.BOOK_ISBN_NOT_FOUND, isbn));
        }

        bookDTO.setIsbn(isbn);
        return saveBook(bookDTO);
    }

    @Override
    public void deleteBookByIsbn(String isbn) {
        bookRepository.deleteById(isbn);
    }

    private Page<BookDTO> getBookDTOList(Page<Book> books) {
        return new PageImpl<>(books.stream()
                .map(Book::toDTO)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private boolean isBookValid(String isbn) {
        return bookRepository.findById(isbn).isPresent();
    }
}
