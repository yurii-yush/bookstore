package com.bookstore.entity;

import com.bookstore.controller.dto.OrderDTO;
import com.bookstore.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SoldItem> books;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant datetime;

    public OrderDTO toDTO() {
        books.forEach(book -> book.setOrder(this));
        return OrderDTO.builder().id(id).books(books).client(client).datetime(datetime).status(status).build();
    }
}
