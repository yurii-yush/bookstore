package com.bookstore.controller.request;

import com.bookstore.annotations.CountryValidationIfNotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthorSearchRequest extends SearchRequest {

    private String surname;

    @CountryValidationIfNotNull
    private String country;
}
