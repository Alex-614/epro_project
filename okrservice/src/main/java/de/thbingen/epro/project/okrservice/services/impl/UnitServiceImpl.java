package de.thbingen.epro.project.okrservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UnitNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
import de.thbingen.epro.project.okrservice.services.UnitService;

@Service
public class UnitServiceImpl implements UnitService {




    private UnitRepository unitRepository;

    private BusinessUnitService businessUnitService;

    @Autowired
    public UnitServiceImpl(UnitRepository unitRepository, BusinessUnitService businessUnitService) {
        this.unitRepository = unitRepository;
        this.businessUnitService = businessUnitService;
    }



    @Override
    public UnitDto createUnit(long companyId, long businessUnitId, UnitDto unitDto) throws UnitAlreadyExistsException, BusinessUnitNotFoundException {
        BusinessUnit businessUnit = businessUnitService.findBusinessUnit(companyId, businessUnitId);

        if (unitRepository.existsByNameAndBusinessUnitIdEquals(unitDto.getName(), businessUnit.getId())) {
            throw new UnitAlreadyExistsException();
        }

        Unit unit = new Unit();
        unit.setName(unitDto.getName());
        unit.setBusinessUnit(businessUnit);

        unitRepository.save(unit);

        return unit.toDto();
    }

    @Override
    public List<UnitDto> findAllUnits(long companyId, long businessUnitId) {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId, companyId);
        List<Unit> units = unitRepository.findByBusinessUnitId(businessUnitIdObject);
        return units.stream().map(m -> m.toDto()).collect(Collectors.toList());
    }

    @Override
    public Unit findUnit(long companyId, long businessUnitId, long unitId) throws UnitNotFoundException {
        return unitRepository.findById(convertId(companyId, businessUnitId, unitId)).orElseThrow(() -> new UnitNotFoundException());
    }

    @Override
    public UnitDto patchUnit(long companyId, long businessUnitId, long unitId, UnitDto unitDto) throws UnitNotFoundException {
        Unit unit = findUnit(companyId, businessUnitId, unitId);

        if (unitDto.getName() != null && !unitDto.getName().isBlank())
            if (unitRepository.existsByNameAndBusinessUnitIdEquals(unitDto.getName(), unit.getBusinessUnit().getId()))
                unit.setName(unitDto.getName());

        unitRepository.save(unit);
        return unit.toDto();
    }

    @Override
    public void deleteUnit(long companyId, long businessUnitId, long unitId) {
        unitRepository.deleteById(convertId(companyId, businessUnitId, unitId));
    }

    @NonNull
    private UnitId convertId(long companyId, long businessUnitId, long unitId) {
        return new UnitId(unitId, new BusinessUnitId(businessUnitId, companyId));
    }























    
}
