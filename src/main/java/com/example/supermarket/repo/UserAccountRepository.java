package com.example.supermarket.repo;

import com.example.supermarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<User,Long> {
    User findByCardId(long parseLong);

    User findByMobileNumber(String parseLong);

    @Query("SELECT u FROM User u WHERE u.purchasePoints > 0")
    List<User> findUsersWithPositivePurchasePoints();
}
