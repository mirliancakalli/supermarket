package com.example.supermarket.repo;

import com.example.supermarket.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    @Query(value = "SELECT * FROM purchases p WHERE p.user_id = :userId ORDER BY p.purchase_date DESC LIMIT 1", nativeQuery = true)
    Purchase findLastPurchaseByUserId(Long userId);
}
