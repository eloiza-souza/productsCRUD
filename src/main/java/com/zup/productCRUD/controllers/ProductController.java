package com.zup.productCRUD.controllers;

import com.zup.productCRUD.dtos.ProductRequest;
import com.zup.productCRUD.dtos.ProductResponse;
import com.zup.productCRUD.mappers.ProductMapper;
import com.zup.productCRUD.models.Product;
import com.zup.productCRUD.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
        Product product = new ProductMapper().toEntity(productRequest);
        productService.save(product);
        ProductResponse productResponse = new ProductMapper().toResponse(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }
}
