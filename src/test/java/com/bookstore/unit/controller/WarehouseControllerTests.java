package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.WarehouseController;
import com.bookstore.controller.dto.StockItemDTO;
import com.bookstore.controller.request.WarehouseSearchRequest;
import com.bookstore.service.WarehouseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
public class WarehouseControllerTests {

    @MockBean
    private WarehouseService warehouseService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private StockItemDTO stockItem;

    @BeforeEach
    public void setup() {
        stockItem = new StockItemDTO(1L, "978-617-679-145-4", 25.3, 10);
    }

    @Test
    public void givenValidSearchRequest_whenSearchStockItem_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        WarehouseSearchRequest searchRequest = new WarehouseSearchRequest();
        searchRequest.setBookIsbn("617-679");
        Page<StockItemDTO> expectedPage = new PageImpl<>(List.of(stockItem), pagination, 1);

        Mockito.when(warehouseService.searchStockBalances(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.WAREHOUSE_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenNotValidPrice_whenSaveStockItem_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        stockItem.setPrice(0.2);

        //when
        performSaveNotValidStockItem(stockItem);
    }

    @Test
    public void givenNotValidQuantity_whenSaveStockItem_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        stockItem.setQuantity(0);

        //when
        performSaveNotValidStockItem(stockItem);
    }

    @Test
    public void givenValidStockItem_whenSaveStockItem_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(warehouseService.saveStockItem(Mockito.any())).thenReturn(stockItem);
        URI location = URI.create(String.format(Messages.CREATED_WAREHOUSE_URI, stockItem.getId()));

        //when
        mockMvc.perform(
                        post(Messages.WAREHOUSE_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(stockItem))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));
    }

    @Test
    public void givenValidStockItem_whenUpdateStockItem_thenUpdatedStockItem_andStatus200() throws Exception {
        //given
        String price = "58.6";
        String quantity = "10";
        Mockito.when(warehouseService.updateStockItem(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(stockItem);
        stockItem.setQuantity(20);
        stockItem.setPrice(58.6);

        //when
        mockMvc.perform(
                        put(Messages.WAREHOUSE_CONTROLLER_URI + Messages.ISBN_MAPPING, stockItem.getBookIsbn())
                                .param("price", price)
                                .param("quantity", quantity))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(stockItem)));
    }

    private void performSaveNotValidStockItem(StockItemDTO StockItem) throws Exception {
        mockMvc.perform(
                        post(Messages.WAREHOUSE_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(StockItem))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
