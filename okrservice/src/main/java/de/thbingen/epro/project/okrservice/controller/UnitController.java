package de.thbingen.epro.project.okrservice.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import jakarta.validation.Valid;

@RestController
public class UnitController {

    private BusinessUnitRepository businessUnitRepository;
    
    private UnitRepository unitRepository;


    @Autowired
    public UnitController(BusinessUnitRepository businessUnitRepository, UnitRepository unitRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.unitRepository = unitRepository;
    }


    @PostMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number businessUnitId, 
                                                @RequestBody @Valid UnitDto unitDto) {
        BusinessUnitId buId = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        if (!businessUnitRepository.existsById(buId)) {
            // "BusinessUnit not found!"
            unitDto.setName("BusinessUnit not found!");
            return new ResponseEntity<>(unitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (unitRepository.existsByNameAndBusinessUnitIdEquals(unitDto.getName(), buId)) {
            // "Unit already exists!"
            unitDto.setName("Unit already exists!");
            return new ResponseEntity<>(unitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BusinessUnit businessUnit = businessUnitRepository.findById(buId).get();

        Unit unit = new Unit();
        unit.setName(unitDto.getName());
        unit.setBusinessUnit(businessUnit);

        unitRepository.save(unit);

        unitDto.setId(unit.getId());
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }



    
}
