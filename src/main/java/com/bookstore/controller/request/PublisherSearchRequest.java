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
public class PublisherSearchRequest extends SearchRequest {

    private String title;

    @CountryValidationIfNotNull
    private String country;
}
