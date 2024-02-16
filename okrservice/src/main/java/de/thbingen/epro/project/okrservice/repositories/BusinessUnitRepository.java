package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, BusinessUnitId> {
    boolean existsByNameAndCompanyIdEquals(String name, Long companyId);
}
