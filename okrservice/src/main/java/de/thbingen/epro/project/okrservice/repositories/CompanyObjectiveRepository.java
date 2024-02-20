package de.thbingen.epro.project.okrservice.repositories;

import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyObjectiveRepository extends JpaRepository<CompanyObjective, Long> {
    List<CompanyObjective> findByCompanyId(Long companyId);
}
