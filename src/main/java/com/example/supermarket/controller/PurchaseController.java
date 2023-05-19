package com.example.supermarket.controller;

import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.dto.SelectionEnum;
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

    @PostMapping("/{user}/{cashierId}")
    public ResponseEntity<Void> addPurchase(@RequestParam SelectionEnum userData,
                                            @PathVariable(name = "user") String value,
                                            @PathVariable(name = "cashierId") Long cashierId,
                                            @RequestBody PurchasePointsRequest request) {
        purchasePointsService.addPurchase(userData,value, request, cashierId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{user}/{cashierId}")
    public ResponseEntity<?> calculatePurchase(@RequestParam SelectionEnum userData,
                                               @PathVariable(name = "user") String value,
                                               @PathVariable(name = "cashierId") Long cashierId,
                                               @RequestBody PurchasePointsRequest request) throws Exception {
        Map<String, Object> map = purchasePointsService.calculatePurchase(userData,value, request, cashierId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
