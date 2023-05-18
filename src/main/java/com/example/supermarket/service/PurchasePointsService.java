package com.example.supermarket.service;

import com.example.supermarket.entity.User;
import com.example.supermarket.exception.UserNotFoundException;
import com.example.supermarket.repo.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PurchasePointsService {
    private final UserAccountRepository userRepository;

    public PurchasePointsService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addPurchasePoints(Long userId, BigDecimal amountSpent) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        int purchasePoints = calculatePurchasePoints(amountSpent);
        user.addPurchasePoints(purchasePoints);

        userRepository.save(user);
    }

    private int calculatePurchasePoints(BigDecimal amountSpent) {
        return amountSpent.divide(new BigDecimal("5"), RoundingMode.DOWN)
                .multiply(new BigDecimal("10"))
                .intValue();
    }
}
