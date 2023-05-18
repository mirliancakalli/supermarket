package com.example.supermarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "mobile_number",unique = true,nullable = false)
    private String mobileNumber;

    @Column(name = "card_id",unique = true,nullable = false)
    private long cardId;

    private int purchasePoints;

    private BigDecimal balance;

    public User(String name, String surname, String mobileNumber, long cardId) {
        this.name = name;
        this.surname = surname;
        this.mobileNumber = mobileNumber;
        this.cardId = cardId;
    }

    public void addPurchasePoints(int pointsToAdd) {
        this.purchasePoints += pointsToAdd;
    }
}
