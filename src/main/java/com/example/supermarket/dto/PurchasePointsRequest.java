package com.example.supermarket.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchasePointsRequest {
    private BigDecimal amountSpent;
    private RewardType rewardType;
}
