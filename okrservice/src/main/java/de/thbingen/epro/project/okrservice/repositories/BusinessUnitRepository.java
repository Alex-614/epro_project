package de.thbingen.epro.project.okrservice.repositories;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, BusinessUnitId> {
    boolean existsByNameAndCompanyIdEquals(String name, Long companyId);
    List<BusinessUnit> findByCompanyId(Long companyId);
}
