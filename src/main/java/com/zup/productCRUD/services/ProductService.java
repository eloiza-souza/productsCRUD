package com.zup.productCRUD.services;

import com.zup.productCRUD.dtos.ProductRequest;
import com.zup.productCRUD.dtos.ProductResponse;
import com.zup.productCRUD.models.Product;
import com.zup.productCRUD.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse save(ProductRequest productRequest) {
        Product product = mapToEntity(productRequest);
        productRepository.save(product);
        return mapToResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<ProductResponse> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());

        return mapToResponse(productRepository.save(product));
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        productRepository.deleteById(id);
    }

    private Product mapToEntity(ProductRequest productRequest) {
        return new Product(productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStockQuantity(),
                productRequest.getCategory());
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory());
    }
}
