package de.thbingen.epro.project.okrservice.repositories;

import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyKeyResultRepository extends JpaRepository<CompanyKeyResult, Long> {
    List<CompanyKeyResult> findAllByObjectiveId(Long objectiveId);
}
