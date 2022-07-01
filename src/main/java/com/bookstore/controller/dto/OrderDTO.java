package com.bookstore.controller.dto;

import com.bookstore.common.Messages;
import com.bookstore.entity.Client;
import com.bookstore.entity.Order;
import com.bookstore.entity.SoldItem;
import com.bookstore.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    @NotNull
    @Size(min = 1, message = Messages.ADD_BOOKS_TO_ORDER)
    private List<SoldItem> books;

    @NotNull(message = Messages.ADD_CLIENT_TO_ORDER)
    private Client client;

    @DateTimeFormat
    private Instant datetime;

    @Enumerated
    private OrderStatus status;

    public Order toEntity() {
        return Order.builder()
                .id(id)
                .books(books)
                .client(client)
                .datetime(datetime)
                .status(status).build();
    }
}
