package com.bookstore.unit.service;

import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.controller.request.OrderSearchRequest;
import com.bookstore.entity.Client;
import com.bookstore.entity.Order;
import com.bookstore.entity.SoldItem;
import com.bookstore.entity.StockItem;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.exception.CantChangeOrderStatusException;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.WarehouseRepository;
import com.bookstore.service.impl.OrderServiceImpl;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Order order1, order2, order3;
    private StockItem stockItem1, stockItem2, stockItem3;

    @BeforeEach
    public void setup() {
        stockItem1 = new StockItem(1L, "978-617-679-145-4", 25.3, 10);
        stockItem2 = new StockItem(2L, "978-617-8024-01-7", 45.0, 25);
        stockItem3 = new StockItem(3L, "978-618-7807-04-8", 154.5, 23);

        SoldItem item1 = new SoldItem(1L, "978-617-679-145-4", null, 25.3, 2);
        SoldItem item2 = new SoldItem(2L, "978-617-8024-01-7", null, 45.0, 1);
        SoldItem item3 = new SoldItem(3L, "978-617-7807-04-8", null, 154.5, 1);

        Instant time1 = Instant.parse("2022-03-03T00:00:00Z");
        Instant time2 = Instant.parse("2022-04-03T00:00:00Z");
        Instant time3 = Instant.parse("2022-05-03T00:00:00Z");
        Client client = new Client(1L, "Yura", "Yush", "+38(067)-55-55-555", "yura.yush@gmail.com", "Ternopil, vyl Tekstylna 12");
        order1 = new Order(1L, List.of(item1), client, OrderStatus.NEW, time1);
        order2 = new Order(2L, List.of(item2), client, OrderStatus.CANCELLED, time2);
        order3 = new Order(3L, List.of(item3), client, OrderStatus.APPROVED, time3);
    }

    @Test
    public void givenValidSearchRequestWithoutPagination_whenSearchOrder_thenReturnPageWithOrders() {
        //given
        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setStatus(OrderStatus.NEW);

        PageRequest page = PageRequest.of(Pagination.DEFAULT_PAGE, Pagination.DEFAULT_LIMIT);
        Page<Order> pageFromDB = new PageImpl<>(List.of(order1), page, 2L);
        Page<OrderDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<OrderDTO> actualPage = doSearchOrder(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenValidSearchRequestWithPagination_whenSearchOrder_thenReturnPageWithOrders() {
        //given
        OrderSearchRequest searchRequest = new OrderSearchRequest();
        searchRequest.setClientId(1L);
        searchRequest.setPage(0);
        searchRequest.setLimit(2);

        PageRequest page = PageRequest.of(searchRequest.getPage(), searchRequest.getLimit());
        Page<Order> pageFromDB = new PageImpl<>(List.of(order1, order2, order3), page, 2L);
        Page<OrderDTO> expectedPage = getPageDTOList(pageFromDB);

        //when
        Page<OrderDTO> actualPage = doSearchOrder(pageFromDB, searchRequest);

        //then
        assertThat(actualPage, equalTo(expectedPage));
    }

    @Test
    public void givenNotValidOrderStatus_whenUpdateOrder_thenThrowCantChangeOrderStatusException() {
        //given
        when(orderRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(order2));

        //then
        assertThrows(CantChangeOrderStatusException.class, () -> orderService.updateOrder(order2.getId(), order2.toDTO()));

    }

    @Test
    public void givenNotValidOrderStatus_whenUpdateStatus_thenThrowCantChangeOrderStatusException() {
        //given
        when(orderRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(order2));

        //then
        assertThrows(CantChangeOrderStatusException.class, () -> orderService.updateStatus(order2.getId(), OrderStatus.APPROVED));

    }

    @Test
    public void givenNotValidOrderId_whenUpdateOrder_thenThrowEntityNotFoundException() {
        //when
        when(orderRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(order1.getId(), order1.toDTO()));//todo check message
    }

    private Page<OrderDTO> doSearchOrder(Page<Order> pageFromDB, OrderSearchRequest searchRequest) {
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageFromDB);


        return orderService.searchOrders(searchRequest);
    }

    private Page<OrderDTO> getPageDTOList(Page<Order> Orders) {
        return new PageImpl<>(Orders.stream()
                .map(Order::toDTO)
                .collect(Collectors.toList()));
    }
}
