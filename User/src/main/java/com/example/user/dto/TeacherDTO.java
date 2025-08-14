package com.example.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherDTO {
    private Long id;
    private LocalDate dateEmbauche;
    private String grade;
    private String speciality;
    private boolean fullTime;

    private UserDTO user;
}
