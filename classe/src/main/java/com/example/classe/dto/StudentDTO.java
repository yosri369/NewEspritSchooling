package com.example.classe.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDTO {
    private Long id;
    private String registrationNumber;
    private LocalDate enrollmentDate;
    private String status;
    private int academicYear; // <-- add this

    private UserDTO user;  // nested DTO for user info
}
