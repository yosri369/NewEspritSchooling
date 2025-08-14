package com.example.classe.repository;

import com.example.classe.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe,Long> {
    List<Classe> findByAcademicYear(int academicYear);

}
