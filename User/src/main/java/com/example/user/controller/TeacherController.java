package com.example.user.controller;

import com.example.user.dto.TeacherDTO;
import com.example.user.dto.UserDTO;
import com.example.user.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")  // your frontend dev URL
@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(teacher -> {
            TeacherDTO dto = new TeacherDTO();
            dto.setId(teacher.getId());
            dto.setDateEmbauche(teacher.getDateEmbauche());
            dto.setGrade(teacher.getGrade());
            dto.setSpeciality(teacher.getSpeciality());
            dto.setFullTime(teacher.isFullTime());

            if (teacher.getUser() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(teacher.getUser().getFirstname());
                userDTO.setLastname(teacher.getUser().getLastname());
                userDTO.setEmail(teacher.getUser().getEmail());
                userDTO.setPhone(teacher.getUser().getPhone());
                dto.setUser(userDTO);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        return teacherRepository.findById(id).map(teacher -> {
            TeacherDTO dto = new TeacherDTO();
            dto.setId(teacher.getId());
            dto.setDateEmbauche(teacher.getDateEmbauche());
            dto.setGrade(teacher.getGrade());
            dto.setSpeciality(teacher.getSpeciality());
            dto.setFullTime(teacher.isFullTime());

            if (teacher.getUser() != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(teacher.getUser().getFirstname());
                userDTO.setLastname(teacher.getUser().getLastname());
                userDTO.setEmail(teacher.getUser().getEmail());
                userDTO.setPhone(teacher.getUser().getPhone());
                dto.setUser(userDTO);
            }

            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }
}
