package com.example.user.repository;

import com.example.user.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByAcademicYear(int academicYear); // <-- new


}
