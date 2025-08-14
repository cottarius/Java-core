package com.example.limit.repository;

import com.example.limit.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    
    Optional<Limit> findByUserId(Long userId);
    
    @Query("SELECT l FROM Limit l WHERE l.userId = :userId AND l.limitDate = :date")
    Optional<Limit> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Modifying
    @Query("UPDATE Limit l SET l.remainingLimit = l.dailyLimit, l.limitDate = :newDate WHERE l.limitDate < :newDate")
    int resetDailyLimits(@Param("newDate") LocalDate newDate);
    
    @Modifying
    @Query("UPDATE Limit l SET l.remainingLimit = l.remainingLimit - :amount WHERE l.userId = :userId AND l.remainingLimit >= :amount")
    int decreaseLimit(@Param("userId") Long userId, @Param("amount") java.math.BigDecimal amount);
    
    @Modifying
    @Query("UPDATE Limit l SET l.remainingLimit = l.remainingLimit + :amount WHERE l.userId = :userId")
    int increaseLimit(@Param("userId") Long userId, @Param("amount") java.math.BigDecimal amount);
}
