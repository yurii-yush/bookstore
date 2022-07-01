package com.bookstore.controller.request;


import com.bookstore.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderSearchRequest extends SearchRequest {

    private Instant datetimeFrom;
    private Instant datetimeTo;
    private OrderStatus status;
    private Long clientId;

}
