package com.example.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    private Long userId;
    private String userName;
    private BigDecimal balance;
}
