package com.example.user.controller;

import com.example.user.dto.PendingStudentAdminUpdateDTO;
import com.example.user.dto.PendingStudentCreateDTO;
import com.example.user.entity.PendingStudent;
import com.example.user.service.PendingStudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pending-students")
public class PendingStudentController {
    private final PendingStudentService service;

    public PendingStudentController(PendingStudentService service) {
        this.service = service;
    }

    // Create a new PendingStudent
    @PostMapping
    public ResponseEntity<PendingStudent> createPendingStudent(@Valid @RequestBody PendingStudentCreateDTO dto) {
        PendingStudent savedStudent = service.createPendingStudent(dto);
        return ResponseEntity.ok(savedStudent);
    }

    // Admin status update
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingStudent> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody PendingStudentAdminUpdateDTO dto) {

        Optional<PendingStudent> updated = service.updateStatus(id, dto);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PendingStudent> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingStudent> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PendingStudent> getByStatus(@PathVariable String status) {
        return service.findByStatus(status);
    }
}
