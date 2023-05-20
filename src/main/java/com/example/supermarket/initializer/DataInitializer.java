package com.example.supermarket.initializer;

import com.example.supermarket.entity.Cashier;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.CashierRepository;
import com.example.supermarket.repo.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final CashierRepository cashierRepository;

    public DataInitializer(UserAccountRepository userAccountRepository, CashierRepository cashierRepository) {
        this.userAccountRepository = userAccountRepository;
        this.cashierRepository = cashierRepository;
    }

    @Override
    public void run(String... args) {
        // Pre-populate User table
        if(checkIfUserTableIsEmpty()) {
            userAccountRepository.save(createUser("User 1", "Surname 1"));
            userAccountRepository.save(createUser("User 2", "Surname 2"));
            userAccountRepository.save(createUser("User 3", "Surname 3"));
        }
        // Pre-populate Cashier table

        if(checkIfCashierTableIsEmpty()){
            cashierRepository.save(new Cashier("Cashier 1", "Surname 1"));
            cashierRepository.save(new Cashier("Cashier 2", "Surname 2"));
            cashierRepository.save(new Cashier("Cashier 3", "Surname 3"));
        }

    }

    private boolean checkIfUserTableIsEmpty() {
        return userAccountRepository.findAll().size() <= 0;
    }

    private boolean checkIfCashierTableIsEmpty() {
        return cashierRepository.findAll().size() <= 0;
    }

    private User createUser(String name, String surname) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setMobileNumber(generateUniquePhoneNumber());
        user.setCardId(generateUniqueCardId());
        return user;
    }

    private String generateUniquePhoneNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private long generateUniqueCardId() {
        Random random = new Random();
        long cardId;
        boolean isUnique;
        do {
            cardId = random.nextInt(900000) + 100000;
            isUnique = userAccountRepository.findByCardId(cardId) == null;
        } while (!isUnique);
        return cardId;
    }
}

