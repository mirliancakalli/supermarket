package com.example.supermarket.controller;

import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.dto.UserBalanceDTO;
import com.example.supermarket.dto.UserDTO;
import com.example.supermarket.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping
    public ResponseEntity<?> createUserAccount(@RequestBody @Valid UserDTO userDTO){
        return new ResponseEntity<>(userAccountService.createAccount(userDTO), HttpStatus.OK);
    }

    @GetMapping("/{val}")
    public ResponseEntity<?> getUserAccount(@RequestParam SelectionEnum selection, @PathVariable(name = "val") String parameter) {
        return ResponseEntity.ok(userAccountService.getUserAccount(selection, parameter));
    }

    @GetMapping("/balance")
    public ResponseEntity<List<UserBalanceDTO>> getUnclaimedBalances() {
        List<UserBalanceDTO> unclaimedBalances = userAccountService.getUnclaimedBalances();
        return ResponseEntity.ok(unclaimedBalances);
    }
}
