package com.example.supermarket;

import com.example.supermarket.controller.UserAccountController;
import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.dto.UserBalanceDTO;
import com.example.supermarket.dto.UserDTO;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.UserAccountRepository;
import com.example.supermarket.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserAccountControllerTest {

    private UserAccountController userAccountController;

    @Mock
    private UserAccountService userAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAccountController = new UserAccountController(userAccountService);
    }

    @Test
    void testCreateUserAccount() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userAccountService.createAccount(userDTO)).thenReturn(user);

        ResponseEntity<?> response = userAccountController.createUserAccount(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userAccountService, times(1)).createAccount(userDTO);
    }

    @Test
    void testGetUserAccount() throws Exception {
        SelectionEnum selection = SelectionEnum.ID;
        String value = "1";
        User user = new User();
        when(userAccountService.getUserAccount(selection, value)).thenReturn(user);

        ResponseEntity<?> response = userAccountController.getUserAccount(selection, value);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userAccountService, times(1)).getUserAccount(selection, value);
    }

    @Test
    void testGetUserAccount_UserNotFound() throws Exception {
        SelectionEnum selection = SelectionEnum.ID;
        String value = "1";
        when(userAccountService.getUserAccount(selection, value)).thenThrow(new Exception("User not found"));

        assertThrows(Exception.class, () -> userAccountController.getUserAccount(selection, value));

        verify(userAccountService, times(1)).getUserAccount(selection, value);
    }

    @Test
    void testGetUnclaimedBalances() {
        List<UserBalanceDTO> balances = new ArrayList<>();
        when(userAccountService.getUnclaimedBalances()).thenReturn(balances);

        ResponseEntity<List<UserBalanceDTO>> response = userAccountController.getUnclaimedBalances();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balances, response.getBody());
        verify(userAccountService, times(1)).getUnclaimedBalances();
    }
}

class UserAccountServiceTest {

    private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAccountService = new UserAccountService(userAccountRepository);
    }

    @Test
    void testCreateAccount() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userAccountRepository.save(any(User.class))).thenReturn(user);

        User result = userAccountService.createAccount(userDTO);

        assertEquals(user, result);
        verify(userAccountRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserAccount_Id() throws Exception {
        SelectionEnum selection = SelectionEnum.ID;
        String parameter = "1";
        User user = new User();
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userAccountService.getUserAccount(selection, parameter);

        assertEquals(user, result);
        verify(userAccountRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetUserAccount_CardId_UserNotFound() {
        SelectionEnum selection = SelectionEnum.CARD_ID;
        String parameter = "12345";
        when(userAccountRepository.findByCardId(anyLong())).thenReturn(null);

        assertThrows(Exception.class, () -> userAccountService.getUserAccount(selection, parameter));

        verify(userAccountRepository, times(1)).findByCardId(anyLong());
    }

    @Test
    void testGetUserAccount_PhoneNumber_UserNotFound() {
        SelectionEnum selection = SelectionEnum.PHONE_NUMBER;
        String parameter = "123456789";
        when(userAccountRepository.findByMobileNumber(anyString())).thenReturn(null);

        assertThrows(Exception.class, () -> userAccountService.getUserAccount(selection, parameter));

        verify(userAccountRepository, times(1)).findByMobileNumber(anyString());
    }

    @Test
    void testGetUnclaimedBalances() {
        List<User> usersWithPositiveBalances = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        usersWithPositiveBalances.add(user1);
        usersWithPositiveBalances.add(user2);
        when(userAccountRepository.findUsersWithPositivePurchasePoints()).thenReturn(usersWithPositiveBalances);

        List<UserBalanceDTO> result = userAccountService.getUnclaimedBalances();

        assertEquals(2, result.size());
        verify(userAccountRepository, times(1)).findUsersWithPositivePurchasePoints();
    }

    @Test
    void testSave() {
        User user = new User();

        userAccountService.save(user);

        verify(userAccountRepository, times(1)).save(user);
    }
}

