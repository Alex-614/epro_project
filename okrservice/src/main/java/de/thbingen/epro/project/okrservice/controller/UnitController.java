package de.thbingen.epro.project.okrservice.controller;

import java.lang.reflect.Field;
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
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
public class UnitController {
    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;
    
    private UnitRepository unitRepository;


    @Autowired
    public UnitController(BusinessUnitRepository businessUnitRepository, UnitRepository unitRepository, CompanyRepository companyRepository) {
        this.businessUnitRepository = businessUnitRepository;
        this.unitRepository = unitRepository;
        this.companyRepository = companyRepository;
    }


    @PostMapping
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number businessUnitId, 
                                                @RequestBody @Valid UnitDto unitDto) throws Exception {
        BusinessUnit businessUnit = Utils.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
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
        Unit unit = Utils.getUnitFromRepository(companyRepository, companyId, businessUnitRepository, businessUnitId,
                unitRepository, unitId);
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
        Unit oldUnit =
                Utils.getUnitFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, unitRepository, unitId);
        UnitDto oldUnitDto = new UnitDto(oldUnit);

        Field[] fields = UnitDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(unitDto);
            if(value != null) {
                field.set(oldUnitDto, value);
            }
            field.setAccessible(false);
        }
        oldUnit.setName(oldUnitDto.getName());
        unitRepository.save(oldUnit);
        return new ResponseEntity<>(oldUnitDto, HttpStatus.OK);
    }



    @DeleteMapping("{unitId}")
    public ResponseEntity<String> deleteUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = Utils.getUnitFromRepository(companyRepository, companyId, businessUnitRepository, businessUnitId,
                unitRepository, unitId);
        unitRepository.deleteById(unit.getId());
        return new ResponseEntity<>(unit.getName() + " deleted!", HttpStatus.OK);
    }
}
