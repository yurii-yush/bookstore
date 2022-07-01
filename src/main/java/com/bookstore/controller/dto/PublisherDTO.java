package com.bookstore.controller.dto;

import com.bookstore.annotations.CountryValidation;
import com.bookstore.common.Messages;
import com.bookstore.entity.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherDTO {

    private Long id;

    @NotEmpty(message = Messages.NOT_EMPTY_TITLE)
    @Size(min = 3, max = 50, message = Messages.REQUIRED_TITLE_LENGTH)
    private String title;

    @CountryValidation
    private String country;

    public Publisher toEntity() {
        return Publisher.builder()
                .id(id)
                .title(title)
                .country(country).build();
    }
}
