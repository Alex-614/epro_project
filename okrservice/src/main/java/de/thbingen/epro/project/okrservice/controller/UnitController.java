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
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.buisinessunit.BuisinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import de.thbingen.epro.project.okrservice.repositories.BuisinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;

@RestController
public class UnitController {

    private BuisinessUnitRepository buisinessUnitRepository;
    
    private UnitRepository unitRepository;


    @Autowired
    public UnitController(BuisinessUnitRepository buisinessUnitRepository, UnitRepository unitRepository) {
        this.buisinessUnitRepository = buisinessUnitRepository;
        this.unitRepository = unitRepository;
    }


    @PostMapping("/company/{companyId}/buisinessunit/{buisinesssunitId}/unit")
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                                @PathVariable @NonNull Number buisinesssunitId, 
                                                                @RequestBody UnitDto unitDto) {
        BuisinessUnitId buisinessUnitId = new BuisinessUnitId(buisinesssunitId.longValue(), companyId.longValue());
        if (!buisinessUnitRepository.existsById(buisinessUnitId)) {
            // "BuisinessUnit not found!"
            unitDto.setName("BuisinessUnit not found!");
            return new ResponseEntity<>(unitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (unitRepository.existsByNameAndBuisinessUnitIdEquals(unitDto.getName(), buisinessUnitId)) {
            // "Unit already exists!"
            unitDto.setName("Unit already exists!");
            return new ResponseEntity<>(unitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BuisinessUnit buisinessUnit = buisinessUnitRepository.findById(buisinessUnitId).get();

        Unit unit = new Unit();
        unit.setName(unitDto.getName());
        unit.setBuisinessUnit(buisinessUnit);

        unitRepository.save(unit);
        unitDto.setId(unit.getId());
        
        return new ResponseEntity<>(unitDto, HttpStatus.OK);
    }



    
}
