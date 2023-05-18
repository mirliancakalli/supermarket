package com.example.supermarket.repo;

import com.example.supermarket.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashierRepository extends JpaRepository<Cashier,Long> {
}
