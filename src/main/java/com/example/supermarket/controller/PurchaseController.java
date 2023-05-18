package com.example.supermarket.controller;

import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.service.PurchasePointsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/purchases")
public class PurchaseController {

    private final PurchasePointsService purchasePointsService;

    public PurchaseController(PurchasePointsService purchasePointsService) {
        this.purchasePointsService = purchasePointsService;
    }

    @PostMapping("/{userId}/{cashierId}")
    public ResponseEntity<Void> addPurchase(@PathVariable(name = "userId") Long userId,@PathVariable(name = "cashierId") Long cashierId, @RequestBody PurchasePointsRequest request){
        purchasePointsService.addPurchase(userId, request.getAmountSpent(),cashierId);
        return ResponseEntity.ok().build();
    }
}
