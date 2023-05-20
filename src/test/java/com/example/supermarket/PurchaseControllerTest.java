package com.example.supermarket;

import com.example.supermarket.controller.PurchaseController;
import com.example.supermarket.dto.PurchasePointsRequest;
import com.example.supermarket.dto.PurchaseResponseDTO;
import com.example.supermarket.dto.RewardType;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.entity.User;
import com.example.supermarket.service.CashierService;
import com.example.supermarket.service.PurchasePointsService;
import com.example.supermarket.service.RedeemPointsService;
import com.example.supermarket.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.Purchase;
import com.example.supermarket.repo.PurchaseRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PurchaseControllerTest {

    private PurchaseController purchaseController;

    @Mock
    private PurchasePointsService purchasePointsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchaseController = new PurchaseController(purchasePointsService);
    }

    @Test
    void testAddPurchase() {
        SelectionEnum userData = SelectionEnum.ID;
        String value = "1";
        Long cashierId = 1L;
        PurchasePointsRequest request = new PurchasePointsRequest();
        PurchaseResponseDTO response = new PurchaseResponseDTO();
        when(purchasePointsService.addPurchase(userData, value, request, cashierId)).thenReturn(response);

        ResponseEntity<?> result = purchaseController.addPurchase(userData, value, cashierId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(purchasePointsService, times(1)).addPurchase(userData, value, request, cashierId);
    }

    @Test
    void testCalculatePurchase() throws Exception {
        SelectionEnum userData = SelectionEnum.ID;
        String value = "1";
        Long cashierId = 1L;
        PurchasePointsRequest request = new PurchasePointsRequest();
        when(purchasePointsService.calculatePurchase(userData, value, request, cashierId)).thenReturn(new HashMap<>());

        ResponseEntity<?> result = purchaseController.calculatePurchase(userData, value, cashierId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new HashMap<>(), result.getBody());
        verify(purchasePointsService, times(1)).calculatePurchase(userData, value, request, cashierId);
    }
}

class PurchasePointsServiceTest {

    private PurchasePointsService purchasePointsService;

    @Mock
    private UserAccountService userService;

    @Mock
    private CashierService cashierService;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private RedeemPointsService redeemPointsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchasePointsService = new PurchasePointsService(userService, cashierService, purchaseRepository, redeemPointsService);
    }

    @Test
    void testAddPurchase_NoRedeems() throws Exception {
        SelectionEnum userData = SelectionEnum.ID;
        String userId = "1";
        PurchasePointsRequest request = new PurchasePointsRequest();
        request.setRewardType(RewardType.DISCOUNT);  // Set the reward type here
        request.setTotalAmountDue(BigDecimal.valueOf(100));  // Set the total amount due here

        Long cashierId = 1L;
        User user = new User();
        user.setPurchasePoints(100);
        Cashier cashier = new Cashier();
        when(cashierService.findById(cashierId)).thenReturn(cashier);
        when(userService.getUserAccount(userData, userId)).thenReturn(user);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(new Purchase());

        PurchaseResponseDTO result = purchasePointsService.addPurchase(userData, userId, request, cashierId);

        assertEquals(0, result.getRemainedPurchasePoints());
        assertEquals(99, result.getPayed());
        assertEquals(100, result.getToPay());
        assertEquals("Discount", result.getRedeemedRewardUsed());
        verify(userService, times(1)).save(user);
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void testCalculatePurchase() throws Exception {
        SelectionEnum userData = SelectionEnum.ID;
        String userId = "1";
        PurchasePointsRequest request = new PurchasePointsRequest();
        request.setRewardType(RewardType.DISCOUNT);  // Set the reward type here
        request.setTotalAmountDue(BigDecimal.valueOf(100));
        Long cashierId = 1L;
        User user = new User();
        user.setPurchasePoints(99);
        when(cashierService.findById(cashierId)).thenReturn(new Cashier());
        when(userService.getUserAccount(userData, userId)).thenReturn(user);

        Map<String, Object> result = purchasePointsService.calculatePurchase(userData, userId, request, cashierId);

        assertEquals(BigDecimal.valueOf(0), result.get("discount"));
        assertNull(result.get("freePacketWater"));
        assertEquals(request.getTotalAmountDue(), result.get("total"));
        verify(cashierService, times(1)).findById(cashierId);
        verify(userService, times(1)).getUserAccount(userData, userId);
    }
}

