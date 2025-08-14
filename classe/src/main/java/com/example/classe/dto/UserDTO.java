package com.example.classe.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
    private String address;
    private String gender;
    private String cin;
    private boolean active;
}
