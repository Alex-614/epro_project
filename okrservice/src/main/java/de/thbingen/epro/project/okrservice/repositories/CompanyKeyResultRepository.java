package de.thbingen.epro.project.okrservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;

public interface CompanyKeyResultRepository extends JpaRepository<CompanyKeyResult, Long> {
    List<CompanyKeyResult> findAllByObjectiveId(Long objectiveId);
}
