package com.example.user.controller;

import com.example.user.dto.StudentDTO;
import com.example.user.dto.UserDTO;
import com.example.user.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:3000")  // your frontend dev URL
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getId());
            dto.setRegistrationNumber(student.getRegistrationNumber());
            dto.setEnrollmentDate(student.getEnrollmentDate());
            dto.setAcademicYear(student.getAcademicYear());
            dto.setStatus(student.getStatus());

            if (student.getUser() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(student.getUser().getFirstname());
                userDTO.setLastname(student.getUser().getLastname());
                userDTO.setEmail(student.getUser().getEmail());
                userDTO.setPhone(student.getUser().getPhone());
                dto.setUser(userDTO);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id).map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getId());
            dto.setRegistrationNumber(student.getRegistrationNumber());
            dto.setEnrollmentDate(student.getEnrollmentDate());
            dto.setAcademicYear(student.getAcademicYear()); // <<< add this
            dto.setStatus(student.getStatus());

            if (student.getUser() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(student.getUser().getFirstname());
                userDTO.setLastname(student.getUser().getLastname());
                userDTO.setEmail(student.getUser().getEmail());
                userDTO.setPhone(student.getUser().getPhone());
                dto.setUser(userDTO);
            }

            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/year/{year}")
    public List<StudentDTO> getStudentsByAcademicYear(@PathVariable int year) {
        return studentRepository.findByAcademicYear(year).stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getId());
            dto.setRegistrationNumber(student.getRegistrationNumber());
            dto.setEnrollmentDate(student.getEnrollmentDate());
            dto.setStatus(student.getStatus());
            dto.setAcademicYear(student.getAcademicYear()); // include in DTO

            if (student.getUser() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(student.getUser().getFirstname());
                userDTO.setLastname(student.getUser().getLastname());
                userDTO.setEmail(student.getUser().getEmail());
                userDTO.setPhone(student.getUser().getPhone());
                dto.setUser(userDTO);
            }

            return dto;
        }).collect(Collectors.toList());

    }
}
