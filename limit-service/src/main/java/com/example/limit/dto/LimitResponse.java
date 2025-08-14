package com.example.limit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LimitResponse {
    private Long userId;
    private BigDecimal dailyLimit;
    private BigDecimal remainingLimit;
    private LocalDate limitDate;
    
    public LimitResponse() {}
    
    public LimitResponse(Long userId, BigDecimal dailyLimit, BigDecimal remainingLimit, LocalDate limitDate) {
        this.userId = userId;
        this.dailyLimit = dailyLimit;
        this.remainingLimit = remainingLimit;
        this.limitDate = limitDate;
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
}
