package com.example.supermarket;

import com.example.supermarket.controller.RedeemPointsController;
import com.example.supermarket.dto.RedeemRequest;
import com.example.supermarket.dto.RewardType;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.RedeemedRewards;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.RedeemedRewardsRepository;
import com.example.supermarket.service.RedeemPointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedeemPointsControllerTest {

    private RedeemPointsController redeemPointsController;

    @Mock
    private RedeemedRewardsRepository redeemedRewardsRepository;

    @Mock
    private RedeemPointsService redeemPointsService;

    @Captor
    private ArgumentCaptor<RedeemedRewards> redeemedRewardsCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redeemPointsController = new RedeemPointsController(redeemPointsService);
    }

    @Test
    void testRedeemPointsForWater() throws Exception {
        SelectionEnum userData = SelectionEnum.ID;
        String userId = "1";
        int pointsToRedeem = 10;
        Long cashierId = 1L;

        RedeemRequest redeemRequest = new RedeemRequest();
        redeemRequest.setPointsToRedeem(pointsToRedeem);

        when(redeemPointsService.redeemPointsForWater(userData, userId, pointsToRedeem, cashierId)).thenReturn(5);

        ResponseEntity<?> response = redeemPointsController.redeemPointsForWater(userData, userId, redeemRequest, cashierId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("message", "Points redeemed for 5 free water packet successfully."), response.getBody());
        verify(redeemPointsService).redeemPointsForWater(userData, userId, pointsToRedeem, cashierId);
    }

    @Test
    public void testCreateRedeemPoints() {
        // Arrange
        RewardType rewardType = RewardType.FREE_WATER_PACKET;
        int pointsRedeemed = 150;
        Cashier cashier = new Cashier();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        RedeemPointsService redeemPointsService = new RedeemPointsService(redeemedRewardsRepository, null, null);
        new RedeemPointsController(redeemPointsService);

        // Act
        redeemPointsService.createRedeemPoints(rewardType, pointsRedeemed, cashier, user);

        // Assert
        verify(redeemedRewardsRepository).save(Mockito.any(RedeemedRewards.class));
        verify(redeemedRewardsRepository).save(redeemedRewardsCaptor.capture());
        RedeemedRewards savedRedeemedReward = redeemedRewardsCaptor.getValue();
        assertEquals(rewardType, savedRedeemedReward.getRewardType());
        assertEquals(pointsRedeemed, savedRedeemedReward.getPointsRedeemed());
        assertEquals(cashier, savedRedeemedReward.getCashier());
        assertEquals(user, savedRedeemedReward.getUser());
        assertEquals(now.getYear(), savedRedeemedReward.getRedemptionDate().getYear());
        assertEquals(now.getMonth(), savedRedeemedReward.getRedemptionDate().getMonth());
        assertEquals(now.getDayOfMonth(), savedRedeemedReward.getRedemptionDate().getDayOfMonth());
        assertEquals(now.getHour(), savedRedeemedReward.getRedemptionDate().getHour());
        assertEquals(now.getMinute(), savedRedeemedReward.getRedemptionDate().getMinute());
    }
}
