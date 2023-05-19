package com.example.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    private Long userId;
    private String userName;
    private int purchasePoints;
}
