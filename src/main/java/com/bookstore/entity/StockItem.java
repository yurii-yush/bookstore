package com.bookstore.entity;

import com.bookstore.controller.dto.StockItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "warehouse")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookIsbn;

    private double price;
    private int quantity;

    public StockItemDTO toDTO() {
        return StockItemDTO.builder()
                .id(id)
                .bookIsbn(bookIsbn)
                .quantity(quantity)
                .price(price).build();
    }
}
