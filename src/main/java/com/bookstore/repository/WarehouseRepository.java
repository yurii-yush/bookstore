package com.bookstore.repository;

import com.bookstore.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<StockItem, Long>, JpaSpecificationExecutor<StockItem> {

    Optional<StockItem> findByBookIsbn(String isbn);
}
