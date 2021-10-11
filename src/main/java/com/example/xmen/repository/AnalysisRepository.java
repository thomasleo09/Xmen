package com.example.xmen.repository;

import com.example.xmen.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String> {
    long countByResult(String result);
}
