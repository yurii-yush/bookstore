package com.bookstore.controller;


import com.bookstore.common.Messages;
import com.bookstore.controller.dto.StockItemDTO;
import com.bookstore.controller.request.WarehouseSearchRequest;
import com.bookstore.service.WarehouseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = Messages.WAREHOUSE_CONTROLLER_URI)
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;


    @ApiOperation(value = "This method is used to search items in Warehouse")
    @GetMapping
    public ResponseEntity<Page<StockItemDTO>> searchStockBalances(@Valid @RequestBody WarehouseSearchRequest searchRequest) {
        Page<StockItemDTO> stockBalances = warehouseService.searchStockBalances(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(stockBalances);
    }

    @ApiOperation(value = "This method is used to save new item in Warehouse")
    @PostMapping
    public ResponseEntity<URI> saveStockItem(@Valid @RequestBody StockItemDTO stockItemDTO) {
        StockItemDTO createdStockItem = warehouseService.saveStockItem(stockItemDTO);
        URI location = URI.create(String.format(Messages.CREATED_WAREHOUSE_URI, createdStockItem.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to update item in Warehouse by ISBN")
    @PutMapping(value = Messages.ISBN_MAPPING)
    public ResponseEntity<StockItemDTO> updateStockItem(@RequestParam(name = Messages.QUANTITY_PATH, required = false) Integer quantity,
                                                        @RequestParam(name = Messages.PRICE_PATH, required = false) Double price,
                                                        @PathVariable(Messages.ISBN_PATH) String bookIsbn) {

        StockItemDTO updatedStockItemDTO = warehouseService.updateStockItem(bookIsbn, quantity, price);

        return ResponseEntity.status(HttpStatus.OK).body(updatedStockItemDTO);
    }

}
