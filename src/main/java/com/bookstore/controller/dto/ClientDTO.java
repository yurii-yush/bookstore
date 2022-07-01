package com.bookstore.controller.dto;

import com.bookstore.common.Messages;
import com.bookstore.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Long id;

    @NotEmpty(message = Messages.NOT_EMPTY_NAME)
    @Size(min = 3, max = 30, message = Messages.REQUIRED_NAME_LENGTH)
    private String name;

    @NotEmpty(message = Messages.NOT_EMPTY_SURNAME)
    @Size(min = 3, max = 40, message = Messages.REQUIRED_SURNAME_LENGTH)
    private String surname;

    @Pattern(regexp = Messages.PHONE_NUMBER_REGEXP, message = Messages.PHONE_NUMBER_PATTERN_MESSAGE)
    private String phoneNumber;

    @Email(message = Messages.NOT_VALID_EMAIL)
    @NotEmpty(message = Messages.NOT_EMPTY_EMAIL)
    private String email;

    @NotEmpty(message = Messages.NOT_EMPTY_ADDRESS)
    @Size(min = 15, max = 50, message = Messages.REQUIRED_ADDRESS_LENGTH)
    private String address;

    public Client toEntity() {
        return Client.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .phoneNumber(phoneNumber)
                .email(email)
                .address(address).build();
    }
}
