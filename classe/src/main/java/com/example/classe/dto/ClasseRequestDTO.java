package com.example.classe.dto;

import com.example.classe.entity.Specialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasseRequestDTO {
    private String name;
    private int academicYear;
    private Specialization specialization;
}
