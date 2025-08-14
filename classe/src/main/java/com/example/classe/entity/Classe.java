package com.example.classe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int academicYear;
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    @ElementCollection
    private List<Long> studentIds = new ArrayList<>();
    @ElementCollection
    private List<Long> teacherIds = new ArrayList<>();
}
