package com.example.supermarket.service;

import com.example.supermarket.entity.Cashier;
import com.example.supermarket.repo.CashierRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CashierService {
    private final CashierRepository cashierRepository;

    public CashierService(CashierRepository cashierRepository) {
        this.cashierRepository = cashierRepository;
    }

    public Cashier findOne(Long cashierId) {
        return cashierRepository.findById(cashierId).orElseThrow();
    }

    public List<Cashier> findAll() {
        return cashierRepository.findAll();
    }

    public Cashier findById(Long cashierId) throws Exception {
        var cashier = cashierRepository.findById(cashierId);
        if (cashier.isEmpty()) {
            throw new Exception("Invalid cashier ID");
        } else return cashier.get();
    }
}
