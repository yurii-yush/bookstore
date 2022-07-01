package com.bookstore.controller.dto;

import com.bookstore.common.Messages;
import com.bookstore.entity.Order;
import com.bookstore.entity.SoldItem;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoldItemDTO {

    private Long id;

    @NotNull(message = Messages.BOOK_MANDATORY)
    private String bookIsbn;

    @ToString.Exclude
    private Order order;

    private double price;

    @DecimalMin(value = "1", message = Messages.MIN_QUANTITY)
    private int quantity;

    public SoldItem toEntity() {
        return SoldItem.builder()
                .id(id)
                .bookIsbn(bookIsbn)
                .price(price)
                .quantity(quantity)
                .order(order).build();
    }
}
