package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UnitNotFoundException;

public interface UnitService {




    UnitDto createUnit(long companyId, long businessUnitId, UnitDto unitDto) throws UnitAlreadyExistsException, BusinessUnitNotFoundException;
    List<UnitDto> findAllUnits(long companyId, long businessUnitId);
    Unit findUnit(long companyId, long businessUnitId, long unitId) throws UnitNotFoundException;
    UnitDto patchUnit(long companyId, long businessUnitId, long unitId, UnitDto unitDto) throws UnitNotFoundException;
    void deleteUnit(long companyId, long businessUnitId, long unitId);





}
