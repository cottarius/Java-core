package com.example.product.mapper;

import com.example.product.dto.ProductDto;
import com.example.product.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductDto toDto(Product p) {
        if (p == null) return null;

        return new ProductDto(
                p.getId(),
                p.getAccountNumber(),
                p.getBalance(),
                p.getProductType() != null ? p.getProductType().name() : null,
                p.getUserId(),
                p.getCreatedAt()
        );
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream().map(this::toDto).toList();
    }
}
