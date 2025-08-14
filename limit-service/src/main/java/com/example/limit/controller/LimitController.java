package com.example.limit.controller;

import com.example.limit.dto.ErrorResponse;
import com.example.limit.dto.LimitResponse;
import com.example.limit.dto.PaymentRequest;
import com.example.limit.service.LimitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/limits")
public class LimitController {
    
    private final LimitService limitService;
    
    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<LimitResponse> getUserLimit(@PathVariable Long userId) {
        LimitResponse response = limitService.getUserLimit(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/payment")
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequest request) {
        boolean success = limitService.processPayment(request);
        
        if (success) {
            return ResponseEntity.ok().body(new LimitResponse(
                    request.getUserId(),
                    null,
                    null,
                    null
            ));
        } else {
            ErrorResponse error = new ErrorResponse(
                    "Insufficient limit for payment",
                    "LIMIT_EXCEEDED"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PostMapping("/{userId}/restore")
    public ResponseEntity<?> restoreLimit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {
        
        boolean success = limitService.restoreLimit(userId, amount);
        
        if (success) {
            return ResponseEntity.ok().body(new LimitResponse(
                    userId,
                    null,
                    null,
                    null
            ));
        } else {
            ErrorResponse error = new ErrorResponse(
                    "Failed to restore limit",
                    "RESTORE_FAILED"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PostMapping("/reset")
    public ResponseEntity<String> resetDailyLimits() {
        limitService.resetDailyLimits();
        return ResponseEntity.ok("Daily limits reset successfully");
    }
}
