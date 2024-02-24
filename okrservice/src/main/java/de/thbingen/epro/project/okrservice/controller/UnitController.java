package de.thbingen.epro.project.okrservice.controller;

import java.util.List;

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
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.services.UnitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
public class UnitController {

    private UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }


    @PostMapping
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number businessUnitId, 
                                                @RequestBody @Valid UnitDto unitDto) throws Exception {
        UnitDto response = unitService.createUnit(companyId.longValue(), businessUnitId.longValue(), unitDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    


    @GetMapping("{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId,
                                           @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = unitService.findUnit(companyId.longValue(), businessUnitId.longValue(), unitId.longValue());
        return new ResponseEntity<>(unit.toDto(), HttpStatus.OK);
    }
    
    
    @GetMapping
    public ResponseEntity<List<UnitDto>> getAllUnits(@PathVariable @NonNull Number companyId,
                                                     @PathVariable @NonNull Number businessUnitId) {
        List<UnitDto> response = unitService.findAllUnits(companyId.longValue(), businessUnitId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("{unitId}")
    public ResponseEntity<UnitDto> patchUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId,
                                             @RequestBody UnitDto unitDto) throws Exception {
        UnitDto response = unitService.patchUnit(companyId.longValue(), businessUnitId.longValue(), unitId.longValue(), unitDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @DeleteMapping("{unitId}")
    public ResponseEntity<Void> deleteUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId) throws Exception{
        unitService.deleteUnit(companyId.longValue(), businessUnitId.longValue(), unitId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
