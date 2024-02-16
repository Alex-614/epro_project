package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {

}
