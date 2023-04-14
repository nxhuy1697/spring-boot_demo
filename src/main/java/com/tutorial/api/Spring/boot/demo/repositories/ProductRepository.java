package com.tutorial.api.Spring.boot.demo.repositories;

import com.tutorial.api.Spring.boot.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List <Product> findByProductName(String productName);
}
