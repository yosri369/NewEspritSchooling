package com.example.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClasse;

    private String name;
    private String level;
    private Date academicYear;
    private int capacity;

    // 👉 ID du prof affecté (venant du microservice user)
    private Long teacherId;

    // 👉 Liste d’IDs des étudiants affectés à cette classe
    @ElementCollection
    private List<Long> studentIds;
}