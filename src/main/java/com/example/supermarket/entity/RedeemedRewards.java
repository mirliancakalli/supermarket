package com.example.supermarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "redeemed_rewards")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedeemedRewards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int pointsRedeemed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType rewardType;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private Cashier cashier;

    @Column(nullable = false)
    private LocalDateTime redemptionDate;

}
