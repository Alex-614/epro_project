package de.thbingen.epro.project.okrservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;

public interface KeyResultRepository<T extends KeyResult> extends JpaRepository<T, Long> {
    List<T> findByObjectiveId(Long objectiveId);
}
