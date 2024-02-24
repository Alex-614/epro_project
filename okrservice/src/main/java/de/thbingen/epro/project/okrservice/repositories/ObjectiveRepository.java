package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;

public interface ObjectiveRepository<T extends Objective> extends JpaRepository<T, Long> {

}
