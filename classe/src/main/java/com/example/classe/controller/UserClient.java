package com.example.classe.controller;

import com.example.classe.config.FeignConfig;
import com.example.classe.dto.StudentDTO;
import com.example.classe.dto.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8051", configuration = FeignConfig.class) // or use service discovery
public interface UserClient {
    @GetMapping("/api/students/{id}")
    StudentDTO getStudentById(@PathVariable("id") Long id);

    @GetMapping("/api/teachers/{id}")
    TeacherDTO getTeacherById(@PathVariable("id") Long id);

    // Add this method to get students by academic year
    @GetMapping("/api/students/year/{year}")
    List<StudentDTO> getStudentsByAcademicYear(@PathVariable("year") int year);
}
