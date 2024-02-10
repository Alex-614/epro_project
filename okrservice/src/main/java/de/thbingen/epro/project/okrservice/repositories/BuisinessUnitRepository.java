package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.buisinessunit.BuisinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;

public interface BuisinessUnitRepository extends JpaRepository<BuisinessUnit, BuisinessUnitId> {
    boolean existsByNameAndCompanyIdEquals(String name, Long companyId);
}
