package com.example.supermarket.controller;

import com.example.supermarket.entity.Cashier;
import com.example.supermarket.service.CashierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "/cashiers")
public class CashierController {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @GetMapping("/{cashierId}")
    public Cashier getCashier(@PathVariable(name = "cashierId") Long cashierId){
        return cashierService.findOne(cashierId);
    }

    @GetMapping()
    public List<Cashier> getCashiers(){
        return cashierService.findAll();
    }
}
