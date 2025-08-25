package com.example.classe.dto;

import com.example.classe.entity.Specialization;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClasseResponseDTO {
    private Long id;
    private String name;
    private int academicYear;
    private Specialization specialization;

    private List<StudentDTO> students;   // Add this
    private List<TeacherDTO> teachers;   // Add this
}
