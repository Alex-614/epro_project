package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;

public interface UnitRepository extends JpaRepository<Unit, UnitId> {
    boolean existsByNameAndBuisinessUnitIdEquals(String name, BuisinessUnitId buisinessUnitId);
}
