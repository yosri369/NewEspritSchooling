package com.example.classe.service;

import com.example.classe.controller.UserClient;
import com.example.classe.dto.ClasseRequestDTO;
import com.example.classe.dto.ClasseResponseDTO;
import com.example.classe.dto.StudentDTO;
import com.example.classe.dto.TeacherDTO;
import com.example.classe.entity.Classe;
import com.example.classe.repository.ClasseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClasseService {
    private final ClasseRepository repository;
    private final UserClient userClient; // Feign client


    public ClasseResponseDTO createClass(ClasseRequestDTO dto) {
        Classe classe = new Classe();
        classe.setName(dto.getName());
        classe.setAcademicYear(dto.getAcademicYear());
        classe.setSpecialization(dto.getSpecialization());

        Classe saved = repository.save(classe);
        return mapToResponse(saved);
    }

    public List<ClasseResponseDTO> getAllClasses() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ClasseResponseDTO getClassById(Long id) {
        Classe classe = repository.findById(id).orElseThrow();
        return mapToResponse(classe);
    }

    public void deleteClass(Long id) {
        repository.deleteById(id);
    }

    private ClasseResponseDTO mapToResponse(Classe c) {
        ClasseResponseDTO dto = new ClasseResponseDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setAcademicYear(c.getAcademicYear());
        dto.setSpecialization(c.getSpecialization());
        return dto;
    }

    public void assignStudentToClass(Long classId, Long studentId) {
        // ✅ Step 1: Validate student exists in User microservice
        StudentDTO student = userClient.getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found in User Service");
        }
        Classe classe = repository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        if (!classe.getStudentIds().contains(studentId)) {
            classe.getStudentIds().add(studentId);
            repository.save(classe);
        }
    }

    public void assignTeacherToClass(Long classId, Long teacherId) {
        // Validate teacher exists in User microservice
        TeacherDTO teacher = userClient.getTeacherById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found in User Service");
        }

        Classe classe = repository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (!classe.getTeacherIds().contains(teacherId)) {
            classe.getTeacherIds().add(teacherId);
            repository.save(classe);
        }
    }

    public void removeStudentFromClass(Long classId, Long studentId) {
        Classe classe = repository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        classe.getStudentIds().remove(studentId);
        repository.save(classe);
    }

    public void removeTeacherFromClass(Long classId, Long teacherId) {
        Classe classe = repository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        classe.getTeacherIds().remove(teacherId);
        repository.save(classe);
    }

    public void autoAssignStudentsToClasses(int academicYear) {
        List<StudentDTO> students = userClient.getStudentsByAcademicYear(academicYear);
        List<Classe> classes = repository.findByAcademicYear(academicYear);

        if (classes.isEmpty()) throw new RuntimeException("No classes available for this year");

        int classIndex = 0;
        for (StudentDTO student : students) {
            boolean assigned = false;
            while (!assigned) {
                Classe currentClass = classes.get(classIndex);
                if (currentClass.getStudentIds().size() < 30) {
                    currentClass.getStudentIds().add(student.getId());
                    repository.save(currentClass);
                    assigned = true;
                } else {
                    classIndex++;
                    if (classIndex >= classes.size()) {
                        throw new RuntimeException("Not enough classes to assign all students");
                    }
                }
            }
        }
    }

}
