package com.bookstore.service;

import com.bookstore.controller.dto.StockItemDTO;
import com.bookstore.controller.request.WarehouseSearchRequest;
import org.springframework.data.domain.Page;

public interface WarehouseService {
    StockItemDTO saveStockItem(StockItemDTO stockItemDTO);

    Page<StockItemDTO> searchStockBalances(WarehouseSearchRequest searchRequest);

    StockItemDTO updateStockItem(String bookIsbn, Integer quantity, Double price);

    StockItemDTO withdrawBookFromStock(String bookIsbn, int quantity);

    double getBookPrice(String bookIsbn);

}
