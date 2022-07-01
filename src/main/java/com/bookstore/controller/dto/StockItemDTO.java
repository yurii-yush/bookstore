package com.bookstore.controller.dto;

import com.bookstore.common.Messages;
import com.bookstore.entity.StockItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockItemDTO {

    private Long id;
    private String bookIsbn;

    @DecimalMin(value = "1.0", message = Messages.MIN_PRICE)
    private double price;

    @Min(value = 1, message = Messages.MIN_QUANTITY)
    private int quantity;

    public StockItem toEntity() {
        return StockItem.builder()
                .id(id)
                .bookIsbn(bookIsbn)
                .quantity(quantity)
                .price(price).build();
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 0);// if quantity < 0, then write 0 in db
    }
}
