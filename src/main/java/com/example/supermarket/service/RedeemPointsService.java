package com.example.supermarket.service;

import com.example.supermarket.dto.RewardType;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.RedeemedRewards;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.RedeemedRewardsRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class RedeemPointsService {

    private final RedeemedRewardsRepository redeemedRewardsRepository;
    private final UserAccountService userService;
    private final CashierService cashierService;

    public RedeemPointsService(RedeemedRewardsRepository redeemedRewardsRepository, UserAccountService userService, CashierService cashierService) {
        this.redeemedRewardsRepository = redeemedRewardsRepository;
        this.userService = userService;
        this.cashierService = cashierService;
    }


    public void createRedeemPoints(RewardType freeWaterPacket, int intValue, Cashier cashier, User user) {
        var redeemReward = new RedeemedRewards();
        redeemReward.setRewardType(freeWaterPacket);
        redeemReward.setPointsRedeemed(intValue);
        redeemReward.setCashier(cashier);
        redeemReward.setUser(user);
        redeemReward.setRedemptionDate(LocalDateTime.now());
        redeemedRewardsRepository.save(redeemReward);
    }

    public int redeemPointsForWater(SelectionEnum userData , String userId, int pointsToRedeem, Long cashierId) throws Exception {
        //validate cashier
        var cashier = cashierService.findById(cashierId);
        //validate user
        var user = userService.getUserAccount(userData, userId);

        int purchasePoints = user.getPurchasePoints();

        if (purchasePoints >= pointsToRedeem) {
            int freePacketWater = purchasePoints / 150;
            if (freePacketWater==0){
                throw new Exception("Requested discount but you don't have enough purchase point. Purchase points available: "+purchasePoints+". Minimum required 150");
            }

            createRedeemPoints(RewardType.FREE_WATER_PACKET,freePacketWater*150,cashier,user);
            user.setPurchasePoints(purchasePoints-freePacketWater*150);
            userService.save(user);

            return freePacketWater;
        } else {
            throw new IllegalArgumentException("Invalid points to redeem or insufficient purchase points");
        }
    }
}
