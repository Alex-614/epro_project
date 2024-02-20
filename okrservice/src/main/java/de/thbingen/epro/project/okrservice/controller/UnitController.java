package de.thbingen.epro.project.okrservice.controller;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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


    @PostMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
    public ResponseEntity<UnitDto> createUnit(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number businessUnitId, 
                                                @RequestBody @Valid UnitDto unitDto) throws Exception {
        BusinessUnit businessUnit = Helper.getBusinessUnitFromRepository(companyRepository, companyId,
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

    @PatchMapping("/company/{companyId}/businessunit/{businessUnitId}/unit/{unitId}")
    public ResponseEntity<UnitDto> patchUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId,
                                             @RequestBody UnitDto unitDto) throws Exception {
        Unit oldUnit =
                Helper.getUnitFromRepository(companyRepository, companyId, businessUnitRepository,
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

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/unit")
    public ResponseEntity<List<UnitDto>> getAllUnits(@PathVariable @NonNull Number companyId,
                                                     @PathVariable @NonNull Number businessUnitId) {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        List<Unit> units = unitRepository.findByBusinessUnitId(businessUnitIdObject);
        return new ResponseEntity<>(units.stream()
                .map(UnitDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/unit/{unitId}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId,
                                           @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = Helper.getUnitFromRepository(companyRepository, companyId, businessUnitRepository, businessUnitId,
                unitRepository, unitId);
        return new ResponseEntity<>(new UnitDto(unit), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/businessunit/{businessUnitId}/unit/{unitId}")
    public ResponseEntity<String> deleteUnit(@PathVariable @NonNull Number companyId,
                                             @PathVariable @NonNull Number businessUnitId,
                                             @PathVariable @NonNull Number unitId) throws Exception{
        Unit unit = Helper.getUnitFromRepository(companyRepository, companyId, businessUnitRepository, businessUnitId,
                unitRepository, unitId);
        unitRepository.deleteById(unit.getId());
        return new ResponseEntity<>(unit.getName() + " deleted!", HttpStatus.OK);
    }
}
