package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.StockItemDTO;
import com.bookstore.entity.StockItem;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.exception.NotEnoughInStockException;
import com.bookstore.repository.WarehouseRepository;
import com.bookstore.repository.specification.WarehouseSpecifications;
import com.bookstore.controller.request.WarehouseSearchRequest;
import com.bookstore.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService, Pagination {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StockItemDTO> searchStockBalances(WarehouseSearchRequest searchRequest) {
        Specification<StockItem> query = WarehouseSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<StockItem> stockItems = warehouseRepository.findAll(query, page);

        return getStockItemDTOList(stockItems);
    }

    @Override
    public StockItemDTO updateStockItem(String bookIsbn, Integer quantity, Double price) {

        StockItemDTO stockItemDTO = getStockItemByBookIsbn(bookIsbn);
        updateStockItemPrice(stockItemDTO, price);
        updateStockItemQuantity(stockItemDTO, quantity);

        return saveStockItem(stockItemDTO);
    }

    @Override
    public StockItemDTO saveStockItem(StockItemDTO stockItemDTO) {
        StockItem createdStockItem = warehouseRepository.saveAndFlush(stockItemDTO.toEntity());

        return createdStockItem.toDTO();
    }

    @Override
    public StockItemDTO withdrawBookFromStock(String bookIsbn, int quantity) {
        StockItemDTO stockItemDTO = getStockItemByBookIsbn(bookIsbn);

        if (!isBookEnoughInWarehouse(stockItemDTO, quantity)) {
            throw new NotEnoughInStockException(String.format(Messages.QUANTITY_BOOKS_IN_WAREHOUSE_NOT_ENOUGH, stockItemDTO.getBookIsbn()));
        }

        stockItemDTO.setQuantity(stockItemDTO.getQuantity() - quantity);

        return saveStockItem(stockItemDTO);
    }

    @Override
    public double getBookPrice(String bookIsbn) {
        StockItemDTO stockItemDTO = getStockItemByBookIsbn(bookIsbn);
        return stockItemDTO.getPrice();
    }

    private boolean isBookEnoughInWarehouse(StockItemDTO stockItemDTO, int quantity) {
        return stockItemDTO.getQuantity() > quantity;
    }

    @Transactional(readOnly = true)
    private StockItemDTO getStockItemByBookIsbn(String bookIsbn) {

        return warehouseRepository.findByBookIsbn(bookIsbn)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(Messages.BOOK_IN_WAREHOUSE_NOT_FOUND, bookIsbn)))
                .toDTO();
    }

    private Page<StockItemDTO> getStockItemDTOList(Page<StockItem> stockItems) {

        return new PageImpl<>(stockItems.stream()
                .map(StockItem::toDTO)
                .collect(Collectors.toList()));
    }

    private void updateStockItemQuantity(StockItemDTO stockItemDTO, Integer quantity) {
        int currentQuantity = stockItemDTO.getQuantity();

        int newQuantityInWarehouse =
                quantity == null || quantity == 0 ? currentQuantity : currentQuantity + quantity;

        stockItemDTO.setQuantity(newQuantityInWarehouse);
    }

    private void updateStockItemPrice(StockItemDTO stockItemDTO, Double price) {
        double newPrice =
                price != null && price > 0 ? price : stockItemDTO.getPrice();

        stockItemDTO.setPrice(newPrice);
    }

}
