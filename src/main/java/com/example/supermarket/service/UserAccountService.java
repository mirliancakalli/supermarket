package com.example.supermarket.service;

import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.dto.UserBalanceDTO;
import com.example.supermarket.dto.UserDTO;
import com.example.supermarket.entity.Purchase;
import com.example.supermarket.entity.User;
import com.example.supermarket.exception.UserNotFoundException;
import com.example.supermarket.exception.CashierNotFoundException;
import com.example.supermarket.repo.CashierRepository;
import com.example.supermarket.repo.PurchaseRepository;
import com.example.supermarket.repo.UserAccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PurchaseRepository purchaseRepository;
    private final CashierRepository cashierRepository;

    public UserAccountService(UserAccountRepository userAccountRepository,PurchaseRepository purchaseRepository,CashierRepository cashierRepository) {
        this.userAccountRepository = userAccountRepository;
        this.purchaseRepository = purchaseRepository;
        this.cashierRepository = cashierRepository;
    }

    public User createAccount(UserDTO userDTO) {
        var user = new User(userDTO.getName(),userDTO.getSurname(),userDTO.getMobileNumber(),userDTO.getCardId());
        return userAccountRepository.save(user);
    }

    public User getUserAccount(SelectionEnum selection, String parameter) {
        return switch (selection) {
            case ID -> userAccountRepository.findById(Long.parseLong(parameter)).orElseThrow();
            case CARD_ID -> userAccountRepository.findByCardId(Long.parseLong(parameter));
            case PHONE_NUMBER -> userAccountRepository.findByMobileNumber(parameter);
        };
    }

    public void redeemPointsForDiscount(Long userId, int pointsToRedeem,Long cashierId) {

        if (!cashierRepository.existsById(cashierId)) {
            throw new CashierNotFoundException("Invalid cashier ID");
        }

        User user = userAccountRepository.findByCardId(userId);

        int purchasePoints = user.getPurchasePoints();

        // Check if the user has enough points to redeem
        if (purchasePoints >= pointsToRedeem) {
            // Get the last purchase for the user
            Purchase lastPurchase = purchaseRepository.findLastPurchaseByUserId(userId);

            // Calculate the discount amount based on the points to redeem
            int discountAmount = pointsToRedeem / 100; // 1 euro discount for every 100 points

            if (lastPurchase != null) {
                // Apply the discount on the last purchase
                BigDecimal lastPurchaseAmount = lastPurchase.getAmount();
                BigDecimal discount = BigDecimal.valueOf(discountAmount);
                BigDecimal discountedAmount = lastPurchaseAmount.subtract(discount);
                lastPurchase.setAmount(discountedAmount);
                purchaseRepository.save(lastPurchase);
            } else {
                throw new IllegalStateException("No previous purchase found for the user");
            }
            user.setPurchasePoints(purchasePoints - pointsToRedeem);
            userAccountRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid points to redeem or insufficient purchase points");
        }
    }

    public void redeemPointsForWater(Long userId, int pointsToRedeem,Long cashierId) {

        if (!cashierRepository.existsById(cashierId)) {
            throw new CashierNotFoundException("Invalid cashier ID");
        }


        User user = userAccountRepository.findByCardId(userId);
        int purchasePoints = user.getPurchasePoints();
        // Check if the user has enough points to redeem
        if (purchasePoints >= pointsToRedeem) {
            // Calculate the number of water packets to give
            int waterPackets = pointsToRedeem / 150; // 1 packet for every 150 points

            user.setPurchasePoints(purchasePoints - pointsToRedeem);
            userAccountRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid points to redeem or insufficient purchase points");
        }
    }

    public List<UserBalanceDTO> getUnclaimedBalances() {
        List<User> usersWithPositiveBalances = userAccountRepository.findUsersWithPositiveBalance();

        List<UserBalanceDTO> unclaimedBalances = new ArrayList<>();
        for (User user : usersWithPositiveBalances) {
            unclaimedBalances.add(new UserBalanceDTO(user.getId(), user.getName(), user.getBalance()));
        }
        return unclaimedBalances;
    }
}
