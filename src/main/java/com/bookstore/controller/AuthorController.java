package com.bookstore.controller;

import com.bookstore.common.Messages;
import com.bookstore.controller.dto.AuthorDTO;
import com.bookstore.controller.request.AuthorSearchRequest;
import com.bookstore.service.AuthorService;
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
@RequestMapping(value = Messages.AUTHOR_CONTROLLER_URI)
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "This method is used to search Authors")
    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> searchAuthor(@Valid @RequestBody AuthorSearchRequest searchRequest) {
        Page<AuthorDTO> authors = authorService.searchAuthor(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authors);
    }

    @ApiOperation(value = "This method is used to save new Author")
    @PostMapping
    public ResponseEntity<URI> saveAuthor(@Valid @RequestBody AuthorDTO author) {
        AuthorDTO createdAuthor = authorService.saveAuthor(author);
        URI location = URI.create(String.format(Messages.CREATED_AUTHOR_URI, createdAuthor.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to delete Author by ID")
    @DeleteMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<?> deleteAuthorById(@PathVariable(Messages.ID_PATH) Long id) {
        authorService.deleteAuthorById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "This method is used to update Author by ID")
    @PutMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<AuthorDTO> updateAuthor(@Valid @RequestBody AuthorDTO author,
                                                  @PathVariable(Messages.ID_PATH) Long authorId) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(author, authorId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
    }

}
