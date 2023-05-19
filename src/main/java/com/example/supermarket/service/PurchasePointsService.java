package com.example.supermarket.service;

import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.dto.PurchaseResponseDTO;
import com.example.supermarket.dto.RewardType;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.Purchase;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.PurchaseRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PurchasePointsService {

    private final static String OPTION_DISCOUNT ="discount";
    private final static String OPTION_FREE_PACKET_WATER ="freePacketWater";
    private final static String TOTAL ="total";

    private final UserAccountService userService;
    private final CashierService cashierService;
    private final PurchaseRepository purchaseRepository;
    private final RedeemPointsService redeemPointsService;

    public PurchasePointsService(UserAccountService userService,
                                 CashierService cashierService,
                                 PurchaseRepository purchaseRepository,
                                 RedeemPointsService redeemPointsService) {
        this.userService = userService;
        this.cashierService = cashierService;
        this.purchaseRepository = purchaseRepository;
        this.redeemPointsService = redeemPointsService;
    }

    @SneakyThrows
    public PurchaseResponseDTO addPurchase(SelectionEnum userData, String userid, PurchasePointsRequest request, Long cashierId) {
        var response = new PurchaseResponseDTO();
        //validate cashier
        var cashier = cashierService.findById(cashierId);
        //validate user
        var user = userService.getUserAccount(userData, userid);

        var hasRedeems = false;

        if (request.getRewardType().equals(RewardType.FREE_WATER_PACKET)
                || request.getRewardType().equals(RewardType.DISCOUNT)) {
            int existingPurchasePoints = user.getPurchasePoints();

            var discountAmount = BigDecimal.valueOf(existingPurchasePoints / 100);
            var freeWaterPacket = BigDecimal.valueOf(existingPurchasePoints / 150);

            if (request.getRewardType().equals(RewardType.FREE_WATER_PACKET) && freeWaterPacket.compareTo(BigDecimal.ZERO) == 0){
                throw new Exception("Requested free packet of water but you don't have enough purchase point");
            }else  if (request.getRewardType().equals(RewardType.FREE_WATER_PACKET)){
                int remainingPurchasePoint = (existingPurchasePoints - (freeWaterPacket.intValue()*150));
                user.setPurchasePoints(remainingPurchasePoint);
                redeemPointsService.createRedeemPoints(RewardType.FREE_WATER_PACKET,freeWaterPacket.intValue()*150,cashier,user);

                response.setUsedPurchasePoints(freeWaterPacket.intValue()*150);
                response.setRedeemedRewardUsed("Free packet water");

                response.setPayed(request.getTotalAmountDue().intValue());
                response.setToPay(request.getTotalAmountDue().intValue());

                response.setRemainedPurchasePoints(remainingPurchasePoint);
                response.setFreePacketWater(freeWaterPacket.intValue());
            }

            if (request.getRewardType().equals(RewardType.DISCOUNT) && discountAmount.compareTo(BigDecimal.ZERO) == 0){
                throw new Exception("Requested discount but you don't have enough purchase point");
            }else  if (request.getRewardType().equals(RewardType.DISCOUNT)){
                int remainingPurchasePoint = (existingPurchasePoints - (discountAmount.intValue()*100));
                user.setPurchasePoints(remainingPurchasePoint);
                redeemPointsService.createRedeemPoints(RewardType.DISCOUNT,discountAmount.intValue()*100,cashier,user);

                response.setUsedPurchasePoints(discountAmount.intValue()*100);
                response.setRedeemedRewardUsed("Discount");

                response.setPayed(request.getTotalAmountDue().intValue() - discountAmount.intValue());
                response.setToPay(request.getTotalAmountDue().intValue());

                response.setRemainedPurchasePoints(remainingPurchasePoint);
                response.setDiscount(discountAmount.intValue());
            }
            hasRedeems = true;
        }

        //calculate purchase points for the new order and add them to the remaining one
        int purchasePoints = calculatePurchasePoints(request.getTotalAmountDue());
        user.addPurchasePoints(purchasePoints);
        userService.save(user);

        //if everything ok, create new purchase
        createPurchase(user,cashier,request.getTotalAmountDue());

        if (!hasRedeems){
            response.setRedeemedRewardUsed("None");
            response.setPayed(request.getTotalAmountDue().intValue());
            response.setToPay(request.getTotalAmountDue().intValue());
            response.setRemainedPurchasePoints(user.getPurchasePoints());
        }
        return response;
    }

    private void createPurchase(User user, Cashier cashier, BigDecimal totalAmountDue) {
        var purchase = new Purchase();
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setUser(user);
        purchase.setCashier(cashier);
        purchase.setAmount(totalAmountDue);
        purchaseRepository.save(purchase);
    }

    private int calculatePurchasePoints(BigDecimal amountSpent) {
        return amountSpent.divide(new BigDecimal("5"), RoundingMode.DOWN)
                .multiply(new BigDecimal("10"))
                .intValue();
    }

    public Map<String, Object> calculatePurchase(SelectionEnum userData, String value, PurchasePointsRequest request, Long cashierId) throws Exception {
        var response = new HashMap<String, Object>();
        cashierService.findById(cashierId); // just to validate cashier ID
        var user = userService.getUserAccount(userData, value);

        int purchasePoints = user.getPurchasePoints();

        var discountAmount = BigDecimal.valueOf(purchasePoints / 100);
        var freeWaterPacket = BigDecimal.valueOf(purchasePoints / 150);

        if (request.getRewardType().equals(RewardType.FREE_WATER_PACKET)) {
            response.put(OPTION_DISCOUNT, null);
            response.put(OPTION_FREE_PACKET_WATER, freeWaterPacket);
            response.put(TOTAL, request.getTotalAmountDue());
        } else if (request.getRewardType().equals(RewardType.DISCOUNT)) {
            response.put(OPTION_DISCOUNT, discountAmount);
            response.put(OPTION_FREE_PACKET_WATER, null);
            response.put(TOTAL, request.getTotalAmountDue());
        } else {
            response.put(OPTION_DISCOUNT, null);
            response.put(OPTION_FREE_PACKET_WATER, null);
            response.put(TOTAL, request.getTotalAmountDue());
        }
        return response;
    }

}
