package com.example.supermarket.service;

import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.Purchase;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.CashierRepository;
import com.example.supermarket.repo.UserAccountRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PurchasePointsService {
    private final UserAccountRepository userRepository;
    private final CashierRepository cashierRepository;

    public PurchasePointsService(UserAccountRepository userRepository,CashierRepository cashierRepository) {
        this.userRepository = userRepository;
        this.cashierRepository = cashierRepository;
    }

    @SneakyThrows
    public void addPurchase(Long userId, BigDecimal amountSpent,Long cashierId) {

        Optional<Cashier> cashier = cashierRepository.findById(cashierId);
        if (cashier.isEmpty()) {
            throw new Exception("Invalid cashier ID");
        }

        var user = userRepository.findByCardId(userId);

        int purchasePoints = calculatePurchasePoints(amountSpent);
        user.addPurchasePoints(purchasePoints);
        userRepository.save(user);

        var purchase = new Purchase();
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setUser(user);
        purchase.setCashier(cashier.orElseThrow());
        purchase.setAmount(amountSpent);
    }

    private int calculatePurchasePoints(BigDecimal amountSpent) {
        return amountSpent.divide(new BigDecimal("5"), RoundingMode.DOWN)
                .multiply(new BigDecimal("10"))
                .intValue();
    }
}
