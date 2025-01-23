package com.zup.productCRUD.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProductRequest() {
        // Cenário positivo: todos os campos válidos
        ProductRequest productRequest = new ProductRequest(
                "Smartphone",
                "Um smartphone de última geração.",
                999.99,
                10,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertTrue(violations.isEmpty(), "Não deve haver violações para um objeto válido.");
    }

    @Test
    void testNameBlank() {
        // Cenário negativo: nome em branco
        ProductRequest productRequest = new ProductRequest(
                "", "Descrição válida.", 999.99, 10, "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // Verifica se há exatamente 2 violações
        assertEquals(2, violations.size());

        // Verifica se as mensagens de erro esperadas estão presentes
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do produto é obrigatório.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do produto deve ter entre 3 e 100 caracteres.")));
    }

    @Test
    void testNameTooShort() {
        // Cenário negativo: nome com menos de 3 caracteres
        ProductRequest productRequest = new ProductRequest(
                "AB",
                "Descrição válida.",
                999.99,
                10,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("O nome do produto deve ter entre 3 e 100 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    void testDescriptionTooLong() {
        // Cenário negativo: descrição com mais de 500 caracteres
        String longDescription = "A".repeat(501);
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                longDescription,
                999.99,
                10,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("A descrição do produto pode ter no máximo 500 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceNull() {
        // Cenário negativo: preço nulo
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                "Descrição válida.",
                null,
                10,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("O preço do produto é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    void testPriceTooLow() {
        // Cenário negativo: preço menor que 0.01
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                "Descrição válida.",
                0.0,
                10,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("O preço do produto deve ser maior que 0.", violations.iterator().next().getMessage());
    }

    @Test
    void testStockQuantityNull() {
        // Cenário negativo: quantidade em estoque nula
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                "Descrição válida.",
                999.99,
                null,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("A quantidade em estoque é obrigatória.", violations.iterator().next().getMessage());
    }

    @Test
    void testStockQuantityNegative() {
        // Cenário negativo: quantidade em estoque negativa
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                "Descrição válida.",
                999.99,
                -1,
                "Eletrônicos"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("A quantidade em estoque deve ser maior ou igual a 0.", violations.iterator().next().getMessage());
    }

    @Test
    void testCategoryBlank() {
        // Cenário negativo: categoria em branco
        ProductRequest productRequest = new ProductRequest(
                "Produto válido", "Descrição válida.", 999.99, 10, ""
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);

        // Verifica se há exatamente 2 violações
        assertEquals(2, violations.size());

        // Verifica se as mensagens de erro esperadas estão presentes
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A categoria do produto é obrigatória.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A categoria deve ser Eletrônicos, Roupas ou Alimentos.")));
    }

    @Test
    void testCategoryInvalid() {
        // Cenário negativo: categoria inválida
        ProductRequest productRequest = new ProductRequest(
                "Produto válido",
                "Descrição válida.",
                999.99,
                10,
                "Móveis"
        );

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("A categoria deve ser Eletrônicos, Roupas ou Alimentos.", violations.iterator().next().getMessage());
    }
}
