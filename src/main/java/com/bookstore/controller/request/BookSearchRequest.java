package com.bookstore.controller.request;

import com.bookstore.entity.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookSearchRequest extends SearchRequest {

    private String isbn;
    private String title;
    private Long authorId;
    private Long publisherId;
    private BookGenre genre;
}
