package com.example.supermarket.service;

import com.example.supermarket.dto.SelectionEnum;
import com.example.supermarket.dto.UserBalanceDTO;
import com.example.supermarket.dto.UserDTO;
import com.example.supermarket.entity.User;
import com.example.supermarket.repo.UserAccountRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public User createAccount(UserDTO userDTO) {
        var user = new User(userDTO.getName(),userDTO.getSurname(),userDTO.getMobileNumber(),userDTO.getCardId());
        return userAccountRepository.save(user);
    }

    public User getUserAccount(SelectionEnum selection, String parameter) throws Exception {
        return switch (selection) {
            case ID -> userAccountRepository.findById(Long.parseLong(parameter)).orElseThrow(() -> new Exception("User not found"));
            case CARD_ID -> {
                User user = userAccountRepository.findByCardId(Long.parseLong(parameter));
                if (user == null) {
                    throw new Exception("User not found");
                }
                yield user;
            }
            case PHONE_NUMBER -> {
                User user = userAccountRepository.findByMobileNumber(parameter);
                if (user == null) {
                    throw new Exception("User not found");
                }
                yield user;
            }
        };
    }

    public List<UserBalanceDTO> getUnclaimedBalances() {
        List<User> usersWithPositiveBalances = userAccountRepository.findUsersWithPositivePurchasePoints();

        List<UserBalanceDTO> unclaimedBalances = new ArrayList<>();
        for (User user : usersWithPositiveBalances) {
            unclaimedBalances.add(new UserBalanceDTO(user.getId(), user.getName(), user.getPurchasePoints()));
        }
        return unclaimedBalances;
    }

    public void save(User user) {
        userAccountRepository.save(user);
    }
}
