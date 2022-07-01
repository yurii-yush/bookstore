package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.StockItemDTO;
import com.bookstore.controller.request.WarehouseSearchRequest;
import com.bookstore.entity.StockItem;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.exception.NotEnoughInStockException;
import com.bookstore.repository.WarehouseRepository;
import com.bookstore.service.impl.WarehouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTests {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private StockItem item1, item2, item3, item4;

    @BeforeEach
    public void setup() {
        item1 = new StockItem(1L, "978-617-679-145-4", 25.3, 10);
        item2 = new StockItem(2L, "978-617-8024-01-7", 45.0, 25);
        item3 = new StockItem(3L, "978-618-7807-04-8", 154.5, 23);
        item4 = new StockItem(4L, "978-966-97821-0-6", 178.4, 20);
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchStockItem_thenReturnPageWithStockItems() {
        //given
        WarehouseSearchRequest searchRequest = new WarehouseSearchRequest();
        searchRequest.setBookIsbn("978-617");

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<StockItem> pageFromDB = new PageImpl<>(List.of(item1, item2), page, 2L);
        Page<StockItemDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<StockItemDTO> actualPage = doSearchStockItem(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchStockItem_thenReturnPageWithStockItems() {
        //given
        WarehouseSearchRequest searchRequest = new WarehouseSearchRequest();
        searchRequest.setPriceFrom(50.2);
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<StockItem> pageFromDB = new PageImpl<>(List.of(item3, item4), page, 2L);
        Page<StockItemDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<StockItemDTO> actualPage = doSearchStockItem(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidStockItem_whenSaveStockItem_thenSaveAndReturnStockItem() throws Exception {
        //given
        when(warehouseRepository.saveAndFlush(Mockito.any()))
                .thenReturn(item1);

        //when
        StockItemDTO actualStockItem = warehouseService.saveStockItem(item1.toDTO());

        //then
        assertThat(item1.toDTO(), equalTo(actualStockItem));
    }

    @Test
    public void givenValidStockItem_whenUpdateStockItem_thenUpdateAndReturnStockItem() {
        //given
        when(warehouseRepository.findByBookIsbn(Mockito.any()))
                .thenReturn(Optional.of(item1));

        when(warehouseRepository.saveAndFlush(Mockito.any()))
                .thenReturn(item1);

        //when
        StockItemDTO actualStockItem = warehouseService.updateStockItem(item1.getBookIsbn(), 10, 22.3);

        //then
        assertThat(item1.toDTO(), equalTo(actualStockItem));
    }

    @Test
    public void givenNotValidIsbn_whenUpdateStockItem_thenThrowsEntityNotFoundException() {

        //then
        assertThrows(EntityNotFoundException.class, () -> warehouseService.updateStockItem(item1.getBookIsbn(), 10, 22.3));//todo check message
    }

    @Test
    public void givenNotValidQuantity_whenWithdrawBookFromStock_thenThrowsNotEnoughInStockException() {
        //when
        when(warehouseRepository.findByBookIsbn(Mockito.any()))
                .thenReturn(Optional.of(item1));

        //then
        assertThrows(NotEnoughInStockException.class, () -> warehouseService.withdrawBookFromStock(item1.getBookIsbn(), 20));//todo check message
    }

    @Test
    public void givenValidQuantity_whenWithdrawBookFromStock_thenUpdateAndReturnStockItem() {
        //given
        int withdrawQuantity = 1;
        item1.setQuantity(item1.getQuantity() - withdrawQuantity);

        when(warehouseRepository.findByBookIsbn(Mockito.any()))
                .thenReturn(Optional.of(item1));

        when(warehouseRepository.saveAndFlush(Mockito.any()))
                .thenReturn(item1);

        //when
        StockItemDTO actualStockItem = warehouseService.withdrawBookFromStock(item1.getBookIsbn(), withdrawQuantity);

        //then
        assertThat(item1.toDTO(), equalTo(actualStockItem));
    }

    private Page<StockItemDTO> doSearchStockItem(Page<StockItem> pageFromDB, WarehouseSearchRequest searchRequest) {
        when(warehouseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return warehouseService.searchStockBalances(searchRequest);
    }

    private Page<StockItemDTO> getPageDTOList(Page<StockItem> StockItems) {
        return new PageImpl<>(StockItems.stream()
                .map(StockItem::toDTO)
                .collect(Collectors.toList()));
    }
}
