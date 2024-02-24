package de.thbingen.epro.project.okrservice.controller.businessunit;

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

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit")
public class BusinessUnitController {

    private BusinessUnitService businessUnitService;


    @Autowired
    public BusinessUnitController(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }




    @PostMapping
    public ResponseEntity<BusinessUnitDto> createBusinessUnit(@PathVariable @NonNull Number companyId, 
                                                                @RequestBody @Valid BusinessUnitDto businessUnitDto
    ) throws CompanyNotFoundException, BusinessUnitNotFoundException, BusinessUnitAlreadyExistsException {
        BusinessUnitDto response = businessUnitService.createBusinessUnit(companyId.longValue(), businessUnitDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<BusinessUnitDto>> getAllBusinessUnit(@PathVariable @NonNull Number companyId){
        List<BusinessUnitDto> response = businessUnitService.findAllBusinessUnits(companyId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @GetMapping("{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> getBusinessUnit(@PathVariable @NonNull Number companyId,
                                              @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = businessUnitService.findBusinessUnit(companyId.longValue(), businessUnitId.longValue());
        return new ResponseEntity<>(businessUnit.toDto(), HttpStatus.OK);
    }


    @PatchMapping("{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> patchBusinessUnit(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @RequestBody BusinessUnitDto businessUnitDto
    ) throws Exception {
        BusinessUnitDto response = businessUnitService.patchBusinessUnit(companyId.longValue(), businessUnitId.longValue(), businessUnitDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("{businessUnitId}")
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        businessUnitService.deleteBusinessUnit(companyId.longValue(), businessUnitId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}