package com.bookstore.controller;


import com.bookstore.common.Messages;
import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.controller.request.OrderSearchRequest;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.service.OrderService;
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
@RequestMapping(value = Messages.ORDER_CONTROLLER_URI)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "This method is used to search Orders")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> searchOrders(@Valid @RequestBody OrderSearchRequest searchRequest) {
        Page<OrderDTO> ordersDTO = orderService.searchOrders(searchRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ordersDTO);
    }

    @ApiOperation(value = "This method is used to save new Order")
    @PostMapping
    public ResponseEntity<URI> saveOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrderDTO = orderService.saveOrder(orderDTO);
        URI location = URI.create(String.format(Messages.CREATED_ORDER_URI, createdOrderDTO.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(location);
    }

    @ApiOperation(value = "This method is used to update Order by ID")
    @PutMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody @Valid OrderDTO orderDTO,
                                                @PathVariable(Messages.ID_PATH) Long orderId) {

        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, orderDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updatedOrderDTO);
    }

    @ApiOperation(value = "This method is used to change Order status")
    @PatchMapping(value = Messages.ID_MAPPING)
    public ResponseEntity<OrderDTO> mergeOrder(@RequestParam(Messages.STATUS) OrderStatus orderStatus,
                                               @PathVariable(Messages.ID_PATH) Long orderId) {

        OrderDTO updatedOrderDTO = orderService.updateStatus(orderId, orderStatus);

        return ResponseEntity.status(HttpStatus.OK).body(updatedOrderDTO);
    }
}
