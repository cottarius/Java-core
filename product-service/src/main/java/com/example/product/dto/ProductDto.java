package com.example.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDto(
        Long id,
        String accountNumber,
        BigDecimal balance,
        String productType,
        Long userId,
        LocalDateTime createdAt
) {}
