package com.bookstore.entity;

import com.bookstore.controller.dto.SoldItemDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "sold_book")
public class SoldItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookIsbn;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    private double price;
    private int quantity;

    public SoldItemDTO toDTO() {
        return SoldItemDTO.builder()
                .id(id)
                .bookIsbn(bookIsbn)
                .price(price)
                .quantity(quantity)
                .order(order).build();
    }
}
