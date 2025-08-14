package com.example.limit.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_limits")
public class Limit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "daily_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyLimit;
    
    @Column(name = "remaining_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal remainingLimit;
    
    @Column(name = "limit_date", nullable = false)
    private LocalDate limitDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (limitDate == null) {
            limitDate = LocalDate.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Limit() {}
    
    public Limit(Long userId, BigDecimal dailyLimit) {
        this.userId = userId;
        this.dailyLimit = dailyLimit;
        this.remainingLimit = dailyLimit;
        this.limitDate = LocalDate.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }
    
    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
    
    public BigDecimal getRemainingLimit() {
        return remainingLimit;
    }
    
    public void setRemainingLimit(BigDecimal remainingLimit) {
        this.remainingLimit = remainingLimit;
    }
    
    public LocalDate getLimitDate() {
        return limitDate;
    }
    
    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
