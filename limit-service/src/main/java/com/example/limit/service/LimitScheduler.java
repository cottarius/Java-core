package com.example.limit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LimitScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(LimitScheduler.class);
    
    private final LimitService limitService;
    
    public LimitScheduler(LimitService limitService) {
        this.limitService = limitService;
    }
    
    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в 00:00
    public void resetDailyLimits() {
        logger.info("Starting daily limit reset at midnight");
        try {
            limitService.resetDailyLimits();
            logger.info("Daily limit reset completed successfully");
        } catch (Exception e) {
            logger.error("Error during daily limit reset", e);
        }
    }
}
