package com.bookstore.unit.controller;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.OrderController;
import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.controller.request.OrderSearchRequest;
import com.bookstore.entity.Client;
import com.bookstore.entity.SoldItem;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.service.OrderService;
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
import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderDTO order1, order2;

    @BeforeEach
    public void setup() {
        SoldItem item1 = new SoldItem(1L, "978-617-679-145-4", null, 25.6, 2);
        SoldItem item2 = new SoldItem(2L, "978-617-8024-01-7", null, 45.6, 1);
        Instant time = Instant.now();
        Client client = new Client(1L, "Yura", "Yush", "+38(067)-55-55-555", "yura.yush@gmail.com", "Ternopil, vyl Tekstylna 12");
        order1 = new OrderDTO(1L, List.of(item1, item2), client, time, OrderStatus.NEW);
        order2 = new OrderDTO(2L, List.of(item1, item2), client, time, OrderStatus.CANCELLED);
    }

    @Test
    public void givenValidSearchRequest_whenSearchOrder_thenReturnPage_andStatus200() throws Exception {
        //given
        PageRequest pagination = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setStatus(OrderStatus.NEW);
        Page<OrderDTO> expectedPage = new PageImpl<>(List.of(order1), pagination, 1);

        Mockito.when(orderService.searchOrders(Mockito.any())).thenReturn(expectedPage);

        //when
        mockMvc.perform(
                        get(Messages.ORDER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(searchRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    public void givenNotValidClient_whenSaveOrder_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        order1.setClient(null);

        //when
        performSaveNotValidOrder(order1);
    }

    @Test
    public void givenNotValidBooks_whenSaveOrder_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        order1.setBooks(null);

        //when
        performSaveNotValidOrder(order1);
    }

    @Test
    public void givenValidOrder_whenSaveOrder_thenReturnURI_andStatus201() throws Exception {
        //given
        Mockito.when(orderService.saveOrder(Mockito.any())).thenReturn(order1);
        URI location = URI.create(String.format(Messages.CREATED_ORDER_URI, order1.getId()));

        //when
        mockMvc.perform(
                        post(Messages.ORDER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(order1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(location)));
    }

    @Test
    public void givenNotValidClient_whenUpdateOrder_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        order1.setClient(null);

        //when
        performUpdateNotValidOrder(order1);
    }

    @Test
    public void givenNotValidBooks_whenUpdateOrder_thenThrowConstraintViolationException_andStatus400() throws Exception {
        //given
        order1.setBooks(null);

        //when
        performUpdateNotValidOrder(order1);
    }

    @Test
    public void givenValidOrder_whenUpdateOrder_thenUpdatedOrder_andStatus200() throws Exception {
        //given
        Mockito.when(orderService.updateOrder(Mockito.any(), Mockito.any())).thenReturn(order1);

        //when
        mockMvc.perform(
                        put(Messages.ORDER_CONTROLLER_URI + Messages.ID_MAPPING, order1.getId())
                                .content(objectMapper.writeValueAsString(order1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order1)));
    }

    @Test
    public void givenValidStatus_whenMergeOrder_thenUpdatedOrder_andStatus200() throws Exception {
        //given
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.APPROVED;

        //when
        mockMvc.perform(
                        patch(Messages.ORDER_CONTROLLER_URI + Messages.ID_MAPPING, orderId)
                                .param(Messages.STATUS, newStatus.toString()))
                .andExpect(status().isOk());
    }

    private void performUpdateNotValidOrder(OrderDTO Order) throws Exception {
        mockMvc.perform(
                        put(Messages.ORDER_CONTROLLER_URI + Messages.ID_MAPPING, Order.getId())
                                .content(objectMapper.writeValueAsString(Order))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }

    private void performSaveNotValidOrder(OrderDTO Order) throws Exception {
        mockMvc.perform(
                        post(Messages.ORDER_CONTROLLER_URI)
                                .content(objectMapper.writeValueAsString(Order))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> ex.getResolvedException().getClass().equals(ConstraintViolationException.class));
    }
}
