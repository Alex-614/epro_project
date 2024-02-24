package de.thbingen.epro.project.okrservice.repositories;

import java.util.List;

import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;

public interface CompanyObjectiveRepository extends ObjectiveRepository<CompanyObjective> {
    List<CompanyObjective> findByCompanyId(Long companyId);
}
