package com.example.supermarket;

import com.example.supermarket.controller.CashierController;
import com.example.supermarket.entity.Cashier;
import com.example.supermarket.service.CashierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CashierControllerTest {

    private CashierController cashierController;

    @Mock
    private CashierService cashierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cashierController = new CashierController(cashierService);
    }

    @Test
    void testGetCashier() {
        Long cashierId = 1L;
        Cashier cashier = new Cashier();
        when(cashierService.findOne(cashierId)).thenReturn(cashier);

        ResponseEntity<?> response = cashierController.getCashier(cashierId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cashier, response.getBody());
        verify(cashierService, times(1)).findOne(cashierId);
    }

    @Test
    void testGetCashiers() {
        Cashier cashier1 = new Cashier();
        Cashier cashier2 = new Cashier();
        List<Cashier> cashiers = Arrays.asList(cashier1, cashier2);
        when(cashierService.findAll()).thenReturn(cashiers);

        ResponseEntity<?> response = cashierController.getCashiers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cashiers, response.getBody());
        verify(cashierService, times(1)).findAll();
    }
}

