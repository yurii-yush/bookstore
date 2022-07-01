package com.bookstore.controller;

import com.bookstore.common.Messages;
import com.bookstore.controller.dto.BookDTO;
import com.bookstore.controller.request.BookSearchRequest;
import com.bookstore.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = Messages.BOOK_CONTROLLER_URI)
public class BookController {
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "This method is used to search Books")
    @GetMapping
    public ResponseEntity<Page<BookDTO>> searchBook(@Valid @RequestBody BookSearchRequest searchRequest) {
        Page<BookDTO> books = bookService.searchBook(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @ApiOperation(value = "This method is used to save new Book")
    @PostMapping
    public ResponseEntity<URI> saveBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.saveBook(bookDTO);
        URI location = URI.create(String.format(Messages.CREATED_BOOK_URI, createdBook.getIsbn()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to update Book by ISBN")
    @PutMapping(value = Messages.ISBN_MAPPING)
    public ResponseEntity<BookDTO> updateBook(@RequestBody
                                              @Valid BookDTO bookDTO,
                                              @PathVariable(Messages.ISBN_PATH) String isbn) {

        BookDTO updatedBook = bookService.updateBook(bookDTO, isbn);

        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    @ApiOperation(value = "This method is used to delete Book by ISBN")
    @DeleteMapping(value = Messages.ISBN_MAPPING)
    public ResponseEntity<?> deleteBookByIsbn(@PathVariable(Messages.ISBN_PATH) String isbn) {
        bookService.deleteBookByIsbn(isbn);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
