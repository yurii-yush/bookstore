package com.bookstore.entity;

import com.bookstore.controller.dto.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String country;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

    public AuthorDTO toDTO() {
        return AuthorDTO.builder()
                .id(id)
                .books(books)
                .country(country)
                .surname(surname)
                .name(name)
                .build();
    }
}
