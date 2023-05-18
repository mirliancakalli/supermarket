package com.example.supermarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String surname;
    @NotNull @NotBlank
    private String mobileNumber;
    @NotNull
    private long cardId;
}
