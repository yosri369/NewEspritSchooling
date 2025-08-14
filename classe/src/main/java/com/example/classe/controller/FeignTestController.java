package com.example.classe.controller;

import com.example.classe.dto.StudentDTO;
import com.example.classe.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class FeignTestController {
    private final UserClient userClient;

    @GetMapping("/student/{id}")
    public StudentDTO testGetStudent(@PathVariable Long id) {
        return userClient.getStudentById(id);
    }

    @GetMapping("/teacher/{id}")
    public TeacherDTO testGetTeacher(@PathVariable Long id) {
        return userClient.getTeacherById(id);
    }
}
