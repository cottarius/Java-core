package com.example.payment.client;

import com.example.payment.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    @Autowired
    public ProductServiceClient(RestTemplate restTemplate, 
                              @Value("${product.service.url:http://localhost:8080}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public Product getProduct(Long productId) {
        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(
                productServiceUrl + "/api/products/" + productId, 
                Product.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get product from product service: " + e.getMessage(), e);
        }
    }

    public List<Product> getProductsByUserId(Long userId) {
        try {
            ResponseEntity<Product[]> response = restTemplate.getForEntity(
                productServiceUrl + "/api/products/user/" + userId, 
                Product[].class
            );
            return List.of(response.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get products by user ID from product service: " + e.getMessage(), e);
        }
    }

    public Product getProductByAccountNumber(String accountNumber) {
        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(
                productServiceUrl + "/api/products/account/" + accountNumber, 
                Product.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get product by account number from product service: " + e.getMessage(), e);
        }
    }
} 