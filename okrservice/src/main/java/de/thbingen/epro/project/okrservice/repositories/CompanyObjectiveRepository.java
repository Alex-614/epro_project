package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;

public interface CompanyObjectiveRepository extends JpaRepository<CompanyObjective, Long> {

}
