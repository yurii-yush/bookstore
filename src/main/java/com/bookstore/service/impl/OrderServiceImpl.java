package com.bookstore.service.impl;

import com.bookstore.common.Messages;
import com.bookstore.common.Pagination;
import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.entity.Order;
import com.bookstore.entity.SoldItem;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.exception.CantChangeOrderStatusException;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.specification.OrderSpecifications;
import com.bookstore.controller.request.OrderSearchRequest;
import com.bookstore.service.OrderService;
import com.bookstore.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService, Pagination {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> searchOrders(OrderSearchRequest searchRequest) {
        Specification<Order> query = OrderSpecifications.generateQuery(searchRequest);
        PageRequest page = PageRequest.of(Optional.ofNullable(searchRequest.getPage()).orElse(DEFAULT_PAGE), Optional.ofNullable(searchRequest.getLimit()).orElse(DEFAULT_LIMIT));
        Page<Order> orders = orderRepository.findAll(query, page);

        return getOrderDTOList(orders);
    }

    @Override
    public OrderDTO updateStatus(Long orderId, OrderStatus newOrderStatus) {
        OrderDTO orderDTO = getOrderById(orderId);

        if (!isOrderStatusValid(orderDTO.getStatus(), newOrderStatus)) {
            throw new CantChangeOrderStatusException(Messages.CANT_CHANGE_ORDER_STATUS);
        }

        orderRepository.updateOrderStatus(newOrderStatus, orderId);

        return getOrderById(orderId);
    }

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        fillInOrderFields(orderDTO);

        withdrawBooksFromWarehouse(orderDTO.getBooks());
        Order savedOrder = orderRepository.save(orderDTO.toEntity());

        return savedOrder.toDTO();
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        OrderDTO currentOrderDTO = getOrderById(orderId);
        OrderStatus currentOrderStatus = currentOrderDTO.getStatus();

        if (!isOrderStatusValid(currentOrderStatus)) {
            throw new CantChangeOrderStatusException(String.format(Messages.ORDER_UPDATE_EXCEPTION, currentOrderStatus));
        }

        orderDTO.setId(orderId);
        returnAllBookFromOrder(currentOrderDTO);

        return saveOrder(orderDTO);
    }

    private boolean isOrderStatusValid(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus.equals(OrderStatus.CANCELLED) || currentStatus.equals(OrderStatus.COMPLETED)) {
            return false;
        }
        return !currentStatus.equals(OrderStatus.APPROVED) || !newStatus.equals(OrderStatus.NEW);
    }

    private boolean isOrderStatusValid(OrderStatus status) {
        return status.equals(OrderStatus.NEW);
    }

    private void fillInOrderFields(OrderDTO orderDTO) {
        setPriceInSoldBooks(orderDTO.getBooks());
        orderDTO.setDatetime(Instant.now());
        orderDTO.setStatus(OrderStatus.NEW);
    }

    private void withdrawBooksFromWarehouse(List<SoldItem> soldBooks) {
        soldBooks.forEach(soldBook ->
                warehouseService.withdrawBookFromStock(soldBook.getBookIsbn(), soldBook.getQuantity()));
    }

    private void setPriceInSoldBooks(List<SoldItem> soldBooks) {
        soldBooks.forEach(soldBook ->
                soldBook.setPrice(warehouseService.getBookPrice(soldBook.getBookIsbn())));
    }

    private void returnAllBookFromOrder(OrderDTO orderDTO) {
        orderDTO.getBooks().forEach(soldBook ->
                warehouseService.updateStockItem(soldBook.getBookIsbn(), soldBook.getQuantity(), null));
    }

    private Page<OrderDTO> getOrderDTOList(Page<Order> orders) {

        return new PageImpl<>(orders.stream()
                .map(Order::toDTO)
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Messages.ORDER_ID_NOT_FOUND, id)))
                .toDTO();
    }
}
