package com.zup.productCRUD.repository;

import com.zup.productCRUD.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
