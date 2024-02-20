package de.thbingen.epro.project.okrservice.repositories;

import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;

import java.util.List;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {
    List<BusinessUnitObjective> findByBusinessUnitId(BusinessUnitId businessUnitId);
}
