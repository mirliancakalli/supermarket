package com.example.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponseDTO {
    private int toPay;
    private int payed;
    private int discount;
    private int freePacketWater;
    private int remainedPurchasePoints;
    private int usedPurchasePoints;
    private String redeemedRewardUsed;
}
