package com.bookstore.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarehouseSearchRequest extends SearchRequest {

    private String bookIsbn;
    private Double priceFrom;
    private Double priceTo;
    private Integer quantity;

}
