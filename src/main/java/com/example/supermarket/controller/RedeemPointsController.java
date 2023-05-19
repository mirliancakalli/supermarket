package com.example.supermarket.controller;

import com.example.supermarket.dto.RedeemRequest;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.service.RedeemPointsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/redeem-points")
public class RedeemPointsController {

    private final RedeemPointsService redeemPointsService;

    public RedeemPointsController(RedeemPointsService redeemPointsService) {
        this.redeemPointsService = redeemPointsService;
    }

    @PostMapping("/{user}/water/{cashierId}")
    public ResponseEntity<String> redeemPointsForWater(  @RequestParam SelectionEnum userData,
                                                         @PathVariable(name = "user") String value,
                                                         @RequestBody RedeemRequest redeemRequest,
                                                         @PathVariable(name = "cashierId") Long cashierId) throws Exception {
        redeemPointsService.redeemPointsForWater(userData,value, redeemRequest.getPointsToRedeem(),cashierId);
        return ResponseEntity.ok("Points redeemed for a free water packet successfully.");
    }
}
