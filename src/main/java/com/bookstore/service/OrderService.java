package com.bookstore.service;

import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.controller.request.OrderSearchRequest;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderDTO> searchOrders(OrderSearchRequest searchRequest);

    OrderDTO saveOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Long orderId, OrderDTO orderStatus);

    OrderDTO updateStatus(Long orderId, OrderStatus orderStatus);
}