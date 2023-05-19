package com.example.supermarket.controller;

import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.service.PurchasePointsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/purchases")
public class PurchaseController {

    private final PurchasePointsService purchasePointsService;

    public PurchaseController(PurchasePointsService purchasePointsService) {
        this.purchasePointsService = purchasePointsService;
    }

    @PostMapping("/{userCardId}/{cashierId}")
    public ResponseEntity<Void> addPurchase(@PathVariable(name = "userCardId") Long userId,@PathVariable(name = "cashierId") Long cashierId, @RequestBody PurchasePointsRequest request){
        purchasePointsService.addPurchase(userId, request.getAmountSpent(),cashierId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{userCardId}/{cashierId}")
    public ResponseEntity<?> calculatePurchase(@PathVariable(name = "userCardId") Long userId,@PathVariable(name = "cashierId") Long cashierId, @RequestBody PurchasePointsRequest request) throws Exception {
        Map<String,Object> map =purchasePointsService.calculatePurchase(userId, request,cashierId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
