package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String accountNumber, BigDecimal balance, Product.ProductType productType, Long userId) {
        Product product = new Product(accountNumber, balance, productType, userId);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    public Product getProductByAccountNumber(String accountNumber) {
        return productRepository.findByAccountNumber(accountNumber);
    }
} 