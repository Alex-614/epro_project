package de.thbingen.epro.project.okrservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
public class UnitController {

    private UnitRepository unitRepository;

    private Utils utils;

    @Autowired
    public UnitController(UnitRepository unitRepository, Utils utils) {
        this.unitRepository = unitRepository;
        this.utils = utils;
    }


    @PostMapping
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number businessUnitId, 
                                                @RequestBody @Valid UnitDto unitDto) throws Exception {
        BusinessUnit businessUnit = utils.getBusinessUnitFromRepository( companyId, businessUnitId);
        if (unitRepository.existsByNameAndBusinessUnitIdEquals(unitDto.getName(), businessUnit.getId())) {
            // "Unit already exists!"
            throw new UnitAlreadyExistsException();
        }

        Unit unit = new Unit();
        unit.setName(unitDto.getName());
        unit.setBusinessUnit(businessUnit);

        unitRepository.save(unit);

        unitDto.setId(unit.getId());
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }
    


    @GetMapping("{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId,
                                           @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = utils.getUnitFromRepository(companyId, businessUnitId, unitId);
        return new ResponseEntity<>(new UnitDto(unit), HttpStatus.OK);
    }
    
    
    @GetMapping
    public ResponseEntity<List<UnitDto>> getAllUnits(@PathVariable @NonNull Number companyId,
                                                     @PathVariable @NonNull Number businessUnitId) {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        List<Unit> units = unitRepository.findByBusinessUnitId(businessUnitIdObject);
        return new ResponseEntity<>(units.stream()
                .map(UnitDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @PatchMapping("{unitId}")
    public ResponseEntity<UnitDto> patchUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId,
                                             @RequestBody UnitDto unitDto) throws Exception {
        Unit unit = utils.getUnitFromRepository(companyId, businessUnitId, unitId);

        if (unitDto.getName() != null) unit.setName(unitDto.getName());

        unitRepository.save(unit);
        return new ResponseEntity<>(new UnitDto(unit), HttpStatus.OK);
    }



    @DeleteMapping("{unitId}")
    public ResponseEntity<Void> deleteUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = utils.getUnitFromRepository(companyId, businessUnitId, unitId);
        unitRepository.deleteById(unit.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
