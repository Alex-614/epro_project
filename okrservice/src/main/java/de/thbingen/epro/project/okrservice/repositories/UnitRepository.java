package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, UnitId> {
    boolean existsByNameAndBusinessUnitIdEquals(String name, BusinessUnitId businessUnitId);
    List<Unit> findByBusinessUnitId(BusinessUnitId businessUnitId);
}
