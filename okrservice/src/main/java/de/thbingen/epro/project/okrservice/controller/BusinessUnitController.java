package de.thbingen.epro.project.okrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
public class BusinessUnitController {

    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    private UserRepository userRepository;


    @Autowired
    public BusinessUnitController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, 
                                    BusinessUnitObjectiveRepository businessUnitObjectiveRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/company/{companyId}/businessunit")
    public ResponseEntity<BusinessUnitDto> createBusinessUnit(@PathVariable @NonNull Number companyId, 
                                                                @RequestBody @Valid BusinessUnitDto businessUnitDto) {
        if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            businessUnitDto.setName("Company not found!");
            return new ResponseEntity<>(businessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        if (businessUnitRepository.existsByNameAndCompanyIdEquals(businessUnitDto.getName(), company.getId())) {
            // "BusinessUnit already exists!"
            businessUnitDto.setName("BusinessUnit already exists!");
            return new ResponseEntity<>(businessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setName(businessUnitDto.getName());
        businessUnit.setCompany(company);
        
        businessUnitRepository.save(businessUnit);

        businessUnitDto.setId(businessUnit.getId());
        return new ResponseEntity<>(businessUnitDto, HttpStatus.OK);
    }
    
    

    @PostMapping("/company/{companyId}/businessunit/{businessUnitId}/objective")
    public ResponseEntity<ObjectiveDto> createBusinessUnitObjective(@PathVariable @NonNull Number companyId, 
                                                                        @PathVariable @NonNull Number businessUnitId, 
                                                                        @RequestBody @Valid ObjectiveDto objectiveDto) {
        BusinessUnitId buId = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        if (!companyRepository.existsById(companyId.longValue())) {
            // Company not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!businessUnitRepository.existsById(buId)) {
            // BusinessUnit not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!userRepository.existsById(objectiveDto.getOwnerId().longValue())) {
            // User not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BusinessUnit businessUnit = businessUnitRepository.findById(buId).get();
        User owner = userRepository.findById(objectiveDto.getOwnerId().longValue()).get();

        if (businessUnit.getObjectives().size() >= 5) {
            // Reached Max Commpany Objectives
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BusinessUnitObjective objective = new BusinessUnitObjective();
        objective.setBusinessUnit(businessUnit);
        objective.setDeadline(objectiveDto.getDeadline());
        objective.setDescription(objectiveDto.getDescription());
        objective.setOwner(owner);
        objective.setTitle(objectiveDto.getTitle());

        businessUnitObjectiveRepository.save(objective);
        
        objectiveDto.setId(objective.getId());
        return new ResponseEntity<>(objectiveDto, HttpStatus.OK);
    }






}
