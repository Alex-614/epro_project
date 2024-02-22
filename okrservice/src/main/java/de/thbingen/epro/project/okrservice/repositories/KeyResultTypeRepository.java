package de.thbingen.epro.project.okrservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;

public interface KeyResultTypeRepository extends JpaRepository<KeyResultType, Long> {
    Optional<KeyResultType> findByName(String name);
}
