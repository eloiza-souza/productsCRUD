package com.zup.productCRUD.services;

import com.zup.productCRUD.dtos.ProductRequest;
import com.zup.productCRUD.dtos.ProductResponse;
import com.zup.productCRUD.exceptions.ProductNotFoundException;
import com.zup.productCRUD.models.Product;
import com.zup.productCRUD.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        // Cenário positivo: salvar um produto
        ProductRequest productRequest = new ProductRequest("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");

        // Criando o objeto Product que será retornado pelo mock
        Product product = new Product("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");
        product.setId(1L); // Definindo o ID manualmente para simular o comportamento do banco de dados

        // Mockando o comportamento do repositório
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Executando o método a ser testado
        ProductResponse response = productService.save(productRequest);

        // Verificações
        assertNotNull(response); // Verifica se a resposta não é nula
      //  assertEquals(1L, response.getId()); // Verifica se o ID retornado é 1
        assertEquals("Smartphone", response.getName()); // Verifica se o nome está correto
        assertEquals("Descrição do produto", response.getDescription()); // Verifica se a descrição está correta
        assertEquals(999.99, response.getPrice()); // Verifica se o preço está correto
        assertEquals(10, response.getStockQuantity()); // Verifica se a quantidade em estoque está correta
        assertEquals("Eletrônicos", response.getCategory()); // Verifica se a categoria está correta

        // Verificando se o método save foi chamado uma vez
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        // Cenário positivo: obter todos os produtos
        Product product1 = new Product("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");
        product1.setId(1L); // Definindo o ID manualmente
        Product product2 = new Product("Notebook", "Descrição do produto", 1999.99, 5, "Eletrônicos");
        product2.setId(2L); // Definindo o ID manualmente

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> responses = productService.getAllProducts();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Cenário positivo: obter produto por ID
        Product product = new Product("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");
        product.setId(1L); // Definindo o ID manualmente

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Smartphone", response.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        // Cenário de exceção: produto não encontrado por ID
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductsByName() {
        // Cenário positivo: obter produtos por nome
        Product product = new Product("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");
        product.setId(1L); // Definindo o ID manualmente

        List<Product> products = Arrays.asList(product);

        when(productRepository.findByNameContainingIgnoreCase("Smartphone")).thenReturn(products);

        List<ProductResponse> responses = productService.getProductsByName("Smartphone");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Smartphone", responses.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Smartphone");
    }

    @Test
    void testUpdateProduct() {
        // Cenário positivo: atualizar um produto
        ProductRequest productRequest = new ProductRequest("Smartphone Atualizado", "Descrição atualizada", 1099.99, 8, "Eletrônicos");
        Product existingProduct = new Product("Smartphone", "Descrição do produto", 999.99, 10, "Eletrônicos");
        existingProduct.setId(1L); // Definindo o ID manualmente
        Product updatedProduct = new Product("Smartphone Atualizado", "Descrição atualizada", 1099.99, 8, "Eletrônicos");
        updatedProduct.setId(1L); // Definindo o ID manualmente

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse response = productService.updateProduct(1L, productRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Smartphone Atualizado", response.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        // Cenário de exceção: atualizar produto não encontrado
        ProductRequest productRequest = new ProductRequest("Smartphone Atualizado", "Descrição atualizada", 1099.99, 8, "Eletrônicos");

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productRequest));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProductById() {
        // Cenário positivo: deletar produto por ID
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProductById(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductByIdNotFound() {
        // Cenário de exceção: deletar produto não encontrado
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(1L));

        verify(productRepository, times(1)).existsById(1L);
    }
}