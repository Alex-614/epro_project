package de.thbingen.epro.project.okrservice.controller.businessunit;

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

import de.thbingen.epro.project.okrservice.controller.Utils;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit")
public class BusinessUnitController {

    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;
    private Utils utils;


    @Autowired
    public BusinessUnitController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, Utils utils) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.utils = utils;
    }




    @PostMapping
    public ResponseEntity<BusinessUnitDto> createBusinessUnit(@PathVariable @NonNull Number companyId, 
                                                                @RequestBody @Valid BusinessUnitDto businessUnitDto
    ) throws CompanyNotFoundException, BusinessUnitNotFoundException, BusinessUnitAlreadyExistsException {
        if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        if (businessUnitRepository.existsByNameAndCompanyIdEquals(businessUnitDto.getName(), company.getId())) {
            // "BusinessUnit already exists!"
            throw new BusinessUnitAlreadyExistsException();
        }


        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setName(businessUnitDto.getName());
        businessUnit.setCompany(company);
        
        businessUnitRepository.save(businessUnit);

        businessUnitDto.setId(businessUnit.getId());
        return new ResponseEntity<>(businessUnitDto, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<BusinessUnitDto>> getAllBusinessUnit(@PathVariable @NonNull Number companyId){
        List<BusinessUnit> businessUnits = businessUnitRepository.findByCompanyId(companyId.longValue());
        return new ResponseEntity<>(businessUnits.stream()
                .map(BusinessUnitDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    
    @GetMapping("{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> getBusinessUnit(@PathVariable @NonNull Number companyId,
                                              @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = utils.getBusinessUnitFromRepository(companyId, businessUnitId);
        return new ResponseEntity<>(new BusinessUnitDto(businessUnit), HttpStatus.OK);
    }


    @PatchMapping("{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> patchBusinessUnit(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @RequestBody BusinessUnitDto businessUnitDto
    ) throws Exception {
        BusinessUnit businessUnit = utils.getBusinessUnitFromRepository(companyId, businessUnitId);

        if (businessUnitDto.getName() != null) businessUnit.setName(businessUnitDto.getName());

        businessUnitRepository.save(businessUnit);
        return new ResponseEntity<>(new BusinessUnitDto(businessUnit), HttpStatus.OK);
    }


    @DeleteMapping("{businessUnitId}")
    public ResponseEntity<Void> deleteBusinessUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = utils.getBusinessUnitFromRepository(companyId, businessUnitId);
        businessUnitRepository.deleteById(businessUnit.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}