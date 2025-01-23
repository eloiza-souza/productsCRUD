package com.zup.productCRUD.controllers;

import com.zup.productCRUD.dtos.ProductRequest;
import com.zup.productCRUD.dtos.ProductResponse;
import com.zup.productCRUD.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        // Cenário positivo: criação de produto com sucesso
        ProductRequest request = new ProductRequest("Product A", "Description A", 100.0, 2,"Roupas");
        ProductResponse response = new ProductResponse(1L, "Product A", "Description A", 100.0, 2, "Roupas");

        when(productService.save(request)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.createProduct(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(productService, times(1)).save(request);
    }

    @Test
    void testGetAllProducts_Success() {
        // Cenário positivo: obter todos os produtos
        List<ProductResponse> products = Arrays.asList(
                new ProductResponse(1L, "Product A", "Description A", 100.0, 1, "Alimentos"),
                new ProductResponse(2L, "Product B", "Description B", 200.0, 2 , "Eletrônicos")
        );

        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<ProductResponse>> result = productController.getAllProducts();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(products, result.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById_Success() {
        // Cenário positivo: obter produto por ID
        Long productId = 1L;
        ProductResponse response =  new ProductResponse(1L, "Product A", "Description A", 100.0, 1, "Alimentos");

        when(productService.getProductById(productId)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        // Cenário negativo: produto não encontrado
        Long productId = 1L;

        when(productService.getProductById(productId)).thenThrow(new RuntimeException("Product not found"));

        try {
            productController.getProductById(productId);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testGetProductsByName_Success() {
        // Cenário positivo: buscar produtos por nome
        String name = "Product A";
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Product A", "Description A", 100.0, 1, "Alimentos")
        );

        when(productService.getProductsByName(name)).thenReturn(products);

        ResponseEntity<List<ProductResponse>> result = productController.getProductsByName(name);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(products, result.getBody());
        verify(productService, times(1)).getProductsByName(name);
    }

    @Test
    void testUpdateProduct_Success() {
        // Cenário positivo: atualizar produto com sucesso
        Long productId = 1L;
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description", 150.0, 2, "Roupas");
        ProductResponse response = new ProductResponse(productId, "Updated Product", "Updated Description", 150.0, 2, "Alimentos");

        when(productService.updateProduct(productId, request)).thenReturn(response);

        ResponseEntity<ProductResponse> result = productController.updateProduct(productId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(productService, times(1)).updateProduct(productId, request);
    }

    @Test
    void testDeleteProduct_Success() {
        // Cenário positivo: deletar produto com sucesso
        Long productId = 1L;

        doNothing().when(productService).deleteProductById(productId);

        ResponseEntity<Void> result = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(productService, times(1)).deleteProductById(productId);
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Cenário negativo: tentar deletar produto inexistente
        Long productId = 1L;

        doThrow(new RuntimeException("Produto não encontrado")).when(productService).deleteProductById(productId);

        try {
            productController.deleteProduct(productId);
        } catch (RuntimeException e) {
            assertEquals("Produto não encontrado", e.getMessage());
        }

        verify(productService, times(1)).deleteProductById(productId);
    }
}
