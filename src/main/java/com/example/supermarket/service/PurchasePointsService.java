package com.example.supermarket.service;

import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.Purchase;
import com.example.supermarket.repo.CashierRepository;
import com.example.supermarket.repo.PurchaseRepository;
import com.example.supermarket.repo.UserAccountRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchasePointsService {

    private final static String OPTION_DISCOUNT ="discount";
    private final static String OPTION_FREE_PACKET_WATER ="freePacketWater";
    private final static String TOTAL ="total";


    private final UserAccountRepository userRepository;
    private final CashierRepository cashierRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchasePointsService(UserAccountRepository userRepository,CashierRepository cashierRepository,PurchaseRepository purchaseRepository) {
        this.userRepository = userRepository;
        this.cashierRepository = cashierRepository;
        this.purchaseRepository = purchaseRepository;
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
        purchaseRepository.save(purchase);
    }

    private int calculatePurchasePoints(BigDecimal amountSpent) {
        return amountSpent.divide(new BigDecimal("5"), RoundingMode.DOWN)
                .multiply(new BigDecimal("10"))
                .intValue();
    }

    public Map<String, Object> calculatePurchase(Long userId, PurchasePointsRequest request, Long cashierId) throws Exception {
        var response = new HashMap<String, Object>();
        var cashier = cashierRepository.findById(cashierId);

        if (cashier.isEmpty()) {
            throw new Exception("Invalid cashier ID");
        }
        var user = userRepository.findByCardId(userId);

        if (user == null){
            throw new Exception("Invalid User ID");
        }

        int purchasePoints = calculatePurchasePoints(BigDecimal.valueOf(user.getPurchasePoints()));
        var discountAmount = BigDecimal.valueOf(purchasePoints / 100);
        var freeWaterPacket =  BigDecimal.valueOf(purchasePoints / 150);
        //discount in euro
        response.put(OPTION_DISCOUNT, discountAmount);
        //Total to pay is used free packet of water
        response.put(OPTION_FREE_PACKET_WATER, request.getAmountSpent().subtract(discountAmount));
        //discount in euro
        response.put(TOTAL, freeWaterPacket);
        return response;
    }
}
