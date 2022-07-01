package com.bookstore.controller.dto;

import com.bookstore.annotations.CountryValidation;
import com.bookstore.common.Messages;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    private Long id;

    @NotEmpty(message = Messages.NOT_EMPTY_NAME)
    @Size(min = 3, max = 30, message = Messages.REQUIRED_NAME_LENGTH)
    private String name;

    @NotEmpty(message = Messages.NOT_EMPTY_SURNAME)
    @Size(min = 3, max = 40, message = Messages.REQUIRED_SURNAME_LENGTH)
    private String surname;

    @CountryValidation
    private String country;

    @JsonIgnore
    @ToString.Exclude
    private List<Book> books;

    public Author toEntity() {
        return Author.builder()
                .id(id)
                .name(name)
                .books(books)
                .surname(surname)
                .country(country).build();
    }
}
