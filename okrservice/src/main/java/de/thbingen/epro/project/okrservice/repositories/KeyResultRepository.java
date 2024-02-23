package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

}
