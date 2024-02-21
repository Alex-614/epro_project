package de.thbingen.epro.project.okrservice.repositories;

import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessUnitKeyResultRepository extends JpaRepository<BusinessUnitKeyResult, Long> {
    List<BusinessUnitKeyResult> findAllByObjectiveId(Long objectiveId);
}
