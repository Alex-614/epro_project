package de.thbingen.epro.project.okrservice.repositories;

import java.util.List;

import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;

public interface BusinessUnitObjectiveRepository extends ObjectiveRepository<BusinessUnitObjective> {
    List<BusinessUnitObjective> findByBusinessUnitId(BusinessUnitId businessUnitId);
}
