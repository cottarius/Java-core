package com.example.limit.service;

import com.example.limit.dto.LimitResponse;
import com.example.limit.dto.PaymentRequest;
import com.example.limit.model.Limit;
import com.example.limit.repository.LimitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LimitService {
    
    private final LimitRepository limitRepository;
    
    @Value("${limit.default.daily}")
    private BigDecimal defaultDailyLimit;
    
    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }
    
    @Transactional
    public LimitResponse getUserLimit(Long userId) {
        Optional<Limit> limitOpt = limitRepository.findByUserId(userId);
        
        if (limitOpt.isPresent()) {
            Limit limit = limitOpt.get();
            // Проверяем, нужно ли сбросить лимит на новый день
            if (!limit.getLimitDate().equals(LocalDate.now())) {
                limit = resetLimitForNewDay(limit);
            }
            return convertToResponse(limit);
        } else {
            // Создаем новый лимит для пользователя
            Limit newLimit = createNewLimit(userId);
            return convertToResponse(newLimit);
        }
    }
    
    @Transactional
    public boolean processPayment(PaymentRequest request) {
        Long userId = request.getUserId();
        BigDecimal amount = request.getAmount();
        
        // Получаем или создаем лимит
        Limit limit = getOrCreateLimit(userId);
        
        // Проверяем, нужно ли сбросить лимит на новый день
        if (!limit.getLimitDate().equals(LocalDate.now())) {
            limit = resetLimitForNewDay(limit);
        }
        
        // Проверяем, достаточно ли лимита
        if (limit.getRemainingLimit().compareTo(amount) < 0) {
            return false;
        }
        
        // Уменьшаем лимит
        int updatedRows = limitRepository.decreaseLimit(userId, amount);
        return updatedRows > 0;
    }
    
    @Transactional
    public boolean restoreLimit(Long userId, BigDecimal amount) {
        int updatedRows = limitRepository.increaseLimit(userId, amount);
        return updatedRows > 0;
    }
    
    @Transactional
    public void resetDailyLimits() {
        LocalDate today = LocalDate.now();
        limitRepository.resetDailyLimits(today);
    }
    
    private Limit getOrCreateLimit(Long userId) {
        return limitRepository.findByUserId(userId)
                .orElseGet(() -> createNewLimit(userId));
    }
    
    private Limit createNewLimit(Long userId) {
        Limit newLimit = new Limit(userId, defaultDailyLimit);
        return limitRepository.save(newLimit);
    }
    
    private Limit resetLimitForNewDay(Limit limit) {
        limit.setRemainingLimit(limit.getDailyLimit());
        limit.setLimitDate(LocalDate.now());
        return limitRepository.save(limit);
    }
    
    private LimitResponse convertToResponse(Limit limit) {
        return new LimitResponse(
                limit.getUserId(),
                limit.getDailyLimit(),
                limit.getRemainingLimit(),
                limit.getLimitDate()
        );
    }
}
