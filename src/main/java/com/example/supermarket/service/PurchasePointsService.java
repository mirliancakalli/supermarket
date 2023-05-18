package com.example.supermarket.service;

import com.example.supermarket.entity.User;
import com.example.supermarket.repo.CashierRepository;
import com.example.supermarket.repo.UserAccountRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PurchasePointsService {
    private final UserAccountRepository userRepository;
    private final CashierRepository cashierRepository;

    public PurchasePointsService(UserAccountRepository userRepository,CashierRepository cashierRepository) {
        this.userRepository = userRepository;
        this.cashierRepository = cashierRepository;
    }

    @SneakyThrows
    public void addPurchasePoints(Long userId, BigDecimal amountSpent,Long cashierId) {

        if (!cashierRepository.existsById(cashierId)) {
            throw new Exception("Invalid cashier ID");
        }

        User user = userRepository.findByCardId(userId);

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
