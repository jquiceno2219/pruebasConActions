package co.edu.cue.pruebasConActions.service;

import co.edu.cue.pruebasConActions.domain.entities.Products;
import co.edu.cue.pruebasConActions.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Products createProduct(Products product) {
        return productRepository.save(product);
    }

    public Products getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElse(null);
    }

    public Products updateProduct(Long productId, Products product) {
        Optional<Products> existingProduct = productRepository.findById(productId);

        if (existingProduct.isPresent()) {
            Products newProduct = existingProduct.get();
            newProduct.setProductName(product.getProductName());
            newProduct.setProductPrice(product.getProductPrice());
            return productRepository.save(newProduct);
        }
        return null;
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Double calculateTotalPriceForProducts(List<Long> productIds, List<Integer> quantities) {
        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Products IDs and quantities must be the same size.");
        }

        double totalPrice = 0.0;

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            int quantity = quantities.get(i);

            Optional<Products> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Products product = productOptional.get();
                totalPrice += product.getProductPrice() * quantity;
            } else {
                throw new IllegalArgumentException("Products with ID " + productId + " not found.");
            }
        }

        return totalPrice;
    }

}
