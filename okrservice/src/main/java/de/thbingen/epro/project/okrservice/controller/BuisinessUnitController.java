package de.thbingen.epro.project.okrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.BuisinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BuisinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.repositories.BuisinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;

@RestController
public class BuisinessUnitController {

    private CompanyRepository companyRepository;

    private BuisinessUnitRepository buisinessUnitRepository;


    @Autowired
    public BuisinessUnitController(CompanyRepository companyRepository, BuisinessUnitRepository buisinessUnitRepository) {
        this.companyRepository = companyRepository;
        this.buisinessUnitRepository = buisinessUnitRepository;
    }


    @PostMapping("/company/{companyId}/buisinessunit")
    public ResponseEntity<BuisinessUnitDto> createBuisinessUnit(@PathVariable @NonNull Number companyId, @RequestBody BuisinessUnitDto buisinessUnitDto) {
        if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            buisinessUnitDto.setName("Company not found!");
            return new ResponseEntity<>(buisinessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        if (buisinessUnitRepository.existsByNameAndCompanyIdEquals(buisinessUnitDto.getName(), company.getId())) {
            // "BuisinessUnit already exists!"
            buisinessUnitDto.setName("BuisinessUnit already exists!");
            return new ResponseEntity<>(buisinessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        BuisinessUnit buisinessUnit = new BuisinessUnit();
        buisinessUnit.setName(buisinessUnitDto.getName());
        buisinessUnit.setCompany(company);
        
        buisinessUnitRepository.save(buisinessUnit);

        buisinessUnitDto.setId(buisinessUnit.getId());
        return new ResponseEntity<>(buisinessUnitDto, HttpStatus.OK);
    }
    
    








}
