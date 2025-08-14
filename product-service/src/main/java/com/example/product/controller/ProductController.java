package com.example.product.controller;

import com.example.product.dto.ProductDto;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductService productService,
                             ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestParam String accountNumber,
                                                    @RequestParam BigDecimal balance,
                                                    @RequestParam Product.ProductType productType,
                                                    @RequestParam Long userId) {
        Product product = productService.createProduct(accountNumber, balance, productType, userId);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        if (product != null) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productMapper.toDtoList(productService.getAllProducts()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDto>> getProductsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(productMapper.toDtoList(productService.getProductsByUserId(userId)));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ProductDto> getProductByAccountNumber(@PathVariable String accountNumber) {
        Product product = productService.getProductByAccountNumber(accountNumber);
        if (product != null) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
