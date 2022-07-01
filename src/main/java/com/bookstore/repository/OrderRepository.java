package com.bookstore.repository;

import com.bookstore.entity.Order;
import com.bookstore.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("update Order o set o.status = :newOrderStatus where o.id = :orderId")
    void updateOrderStatus(OrderStatus newOrderStatus, Long orderId);
}
