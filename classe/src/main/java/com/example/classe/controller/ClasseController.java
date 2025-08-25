package com.example.classe.controller;

import com.example.classe.dto.ClasseRequestDTO;
import com.example.classe.dto.ClasseResponseDTO;
import com.example.classe.entity.Classe;
import com.example.classe.entity.Specialization;
import com.example.classe.service.ClasseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ClasseController {
    private final ClasseService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ClasseResponseDTO createClass(@RequestBody ClasseRequestDTO dto) {
        return service.createClass(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClasseResponseDTO> getAllClasses() {
        return service.getAllClasses();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClasseResponseDTO getClass(@PathVariable Long id) {
        return service.getClassById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClass(@PathVariable Long id) {
        service.deleteClass(id);
    }

    @GetMapping("/specializations")
    public Specialization[] getAllSpecializations() {
        return Specialization.values();
    }

    @PostMapping("/{classId}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        service.assignStudentToClass(classId, studentId);
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void removeStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        service.removeStudentFromClass(classId, studentId);
    }

    @PostMapping("/{classId}/teachers/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        service.assignTeacherToClass(classId, teacherId);
    }

    @DeleteMapping("/{classId}/teachers/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void removeTeacher(@PathVariable Long classId, @PathVariable Long teacherId) {
        service.removeTeacherFromClass(classId, teacherId);
    }

    @PostMapping("/auto-assign/{academicYear}")
    @PreAuthorize("hasRole('ADMIN')")
    public void autoAssign(@PathVariable int academicYear) {
        service.autoAssignStudentsToClasses(academicYear);
    }

}
