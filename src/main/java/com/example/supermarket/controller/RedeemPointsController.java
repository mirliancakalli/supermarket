package com.example.supermarket.controller;

import com.example.supermarket.dto.RedeemRequest;
import com.example.supermarket.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/redeem-points")
public class RedeemPointsController {

    private final UserAccountService userService;

    public RedeemPointsController(UserAccountService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/discount/{cashierId}")
    public ResponseEntity<String> redeemPointsForDiscount(@PathVariable(name = "userId") Long userId, @RequestBody RedeemRequest redeemRequest,@PathVariable(name = "cashierId") Long cashierId) {
        userService.redeemPointsForDiscount(userId, redeemRequest.getPointsToRedeem(),cashierId);
        return ResponseEntity.ok("Points redeemed for a discount successfully.");
    }

    @PostMapping("/{userId}/water/{cashierId}")
    public ResponseEntity<String> redeemPointsForWater(@PathVariable(name = "userId") Long userId, @RequestBody RedeemRequest redeemRequest,@PathVariable(name = "cashierId") Long cashierId) {
        userService.redeemPointsForWater(userId, redeemRequest.getPointsToRedeem(),cashierId);
        return ResponseEntity.ok("Points redeemed for a free water packet successfully.");
    }
}
