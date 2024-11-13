package co.edu.cue.pruebasConActions.controller;

import co.edu.cue.pruebasConActions.domain.entities.Products;
import co.edu.cue.pruebasConActions.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Products> createProduct(@RequestBody Products product) {
        Products createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Get a product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<Products> getProductById(@PathVariable Long productId) {
        Products product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing product by ID
    @PutMapping("/{productId}")
    public ResponseEntity<Products> updateProduct(@PathVariable Long productId, @RequestBody Products product) {
        Products updatedProduct = productService.updateProduct(productId, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product by ID
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // Calculate total price for multiple products
    @PostMapping("/calculate-total")
    public ResponseEntity<Double> calculateTotalPriceForProducts(@RequestBody Map<Long, Integer> productQuantities) {
        List<Long> productIds = productQuantities.keySet().stream().toList();
        List<Integer> quantities = productQuantities.values().stream().toList();

        try {
            Double totalPrice = productService.calculateTotalPriceForProducts(productIds, quantities);
            return ResponseEntity.ok(totalPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
