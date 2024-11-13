package co.edu.cue.pruebasConActions;

import co.edu.cue.pruebasConActions.domain.entities.Products;
import co.edu.cue.pruebasConActions.repositories.ProductRepository;
import co.edu.cue.pruebasConActions.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Products product = new Products();
        product.setProductName("Test Products");
        product.setProductPrice(100.0);

        when(productRepository.save(any(Products.class))).thenReturn(product);

        Products createdProduct = productService.createProduct(product);
        assertNotNull(createdProduct);
        assertEquals("Test Products", createdProduct.getProductName());
        assertEquals(100.0, createdProduct.getProductPrice());
    }

    @Test
    void testGetProductById() {
        Products product = new Products();
        product.setProductId(1L);
        product.setProductName("Test Products");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Products foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
        assertEquals("Test Products", foundProduct.getProductName());
    }

    @Test
    void testUpdateProduct() {
        Products existingProduct = new Products();
        existingProduct.setProductId(1L);
        existingProduct.setProductName("Old Name");
        existingProduct.setProductPrice(50.0);

        Products updatedProductInfo = new Products();
        updatedProductInfo.setProductName("New Name");
        updatedProductInfo.setProductPrice(75.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Products.class))).thenReturn(existingProduct);

        Products updatedProduct = productService.updateProduct(1L, updatedProductInfo);
        assertNotNull(updatedProduct);
        assertEquals("New Name", updatedProduct.getProductName());
        assertEquals(75.0, updatedProduct.getProductPrice());
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCalculateTotalPriceForProducts() {
        Products product1 = new Products();
        product1.setProductId(1L);
        product1.setProductPrice(50.0);

        Products product2 = new Products();
        product2.setProductId(2L);
        product2.setProductPrice(100.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        List<Long> productIds = Arrays.asList(1L, 2L);
        List<Integer> quantities = Arrays.asList(2, 1);

        double totalPrice = productService.calculateTotalPriceForProducts(productIds, quantities);
        assertEquals(200.0, totalPrice);
    }

    @Test
    void testCalculateTotalPriceForProductsWithMissingProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        List<Long> productIds = Arrays.asList(1L);
        List<Integer> quantities = Arrays.asList(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.calculateTotalPriceForProducts(productIds, quantities);
        });

        assertEquals("Products with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testCalculateTotalPriceForProductsWithMismatchedLists() {
        List<Long> productIds = Arrays.asList(1L);
        List<Integer> quantities = Arrays.asList(1, 2);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.calculateTotalPriceForProducts(productIds, quantities);
        });

        assertEquals("Products IDs and quantities must be the same size.", exception.getMessage());
    }
}
