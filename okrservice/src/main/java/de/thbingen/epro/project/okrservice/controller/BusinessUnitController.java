package de.thbingen.epro.project.okrservice.controller;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PatchMapping("/company/{companyId}/businessunit/{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> patchBusinessUnit(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @RequestBody BusinessUnitDto businessUnitDto
    ) throws Exception {
        BusinessUnit oldBusinessUnit = Helper.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        BusinessUnitDto oldBusinessUnitDto = new BusinessUnitDto(oldBusinessUnit);
        Field[] fields = BusinessUnitDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(businessUnitDto);
            if(value != null) {
                field.set(oldBusinessUnitDto, value);
            }
            field.setAccessible(false);
        }
        oldBusinessUnit.setName(oldBusinessUnitDto.getName());
        businessUnitRepository.save(oldBusinessUnit);
        return new ResponseEntity<>(oldBusinessUnitDto, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit")
    public ResponseEntity<List<BusinessUnitDto>> getAllBusinessUnit(){
        List<BusinessUnit> businessUnits = businessUnitRepository.findAll();
        return new ResponseEntity<>(businessUnits.stream()
                .map(BusinessUnitDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> getBusinessUnit(@PathVariable @NonNull Number companyId,
                                              @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = Helper.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        return new ResponseEntity<>(new BusinessUnitDto(businessUnit), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/businessunit/{businessUnitId}")
    public ResponseEntity<String> deleteBusinessUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = Helper.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);
        businessUnitRepository.deleteById(businessUnit.getId());
        return new ResponseEntity<>(businessUnitDto.getName() + " deleted", HttpStatus.OK);
    }

    @PostMapping("/company/{companyId}/businessunit/{businessUnitId}/objective")
    public ResponseEntity<ObjectiveDto> createBusinessUnitObjective(@PathVariable @NonNull Number companyId, 
                                                                        @PathVariable @NonNull Number businessUnitId, 
                                                                        @RequestBody @Valid BusinessUnitObjectiveDto objectiveDto
    ) throws Exception {
        BusinessUnit businessUnit = Helper.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        User owner = Helper.getUserFromRepository(userRepository, objectiveDto.getOwnerId());
        if (businessUnit.getObjectives().size() >= 5) {
            // Reached Max Company Objectives
            throw new MaxCompanyObjectivesReachedException();
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


    @PatchMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
    public ResponseEntity<ObjectiveDto> patchBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody BusinessUnitObjectiveDto objectiveDto
    ) throws Exception {
        BusinessUnitObjective oldObjective =
                Helper.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        BusinessUnitObjectiveDto oldObjectiveDto = new BusinessUnitObjectiveDto(oldObjective);
        User owner = Helper.getUserFromRepository(userRepository, objectiveDto.getOwnerId());


        Field[] fields = ObjectiveDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(objectiveDto);
            if(value != null) {
                field.set(oldObjectiveDto, value);
            }
            field.setAccessible(false);
        }

        oldObjective.setDeadline(oldObjectiveDto.getDeadline());
        oldObjective.setTitle(oldObjectiveDto.getTitle());
        oldObjective.setDescription(oldObjectiveDto.getDescription());
        oldObjective.setOwner(owner);
        businessUnitObjectiveRepository.save(oldObjective);
        return new ResponseEntity<>(oldObjectiveDto, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/objective")
    public ResponseEntity<List<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(){
        //toDO just objectives from company and business unit given
        List<BusinessUnitObjective> businessUnitsObjectives = businessUnitObjectiveRepository.findAll();
        return new ResponseEntity<>(businessUnitsObjectives.stream()
                .map(BusinessUnitObjectiveDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
    public ResponseEntity<BusinessUnitObjectiveDto> getBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId
    ) throws Exception {
        BusinessUnitObjective businessUnitObjective =
                Helper.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        return new ResponseEntity<>(new BusinessUnitObjectiveDto(businessUnitObjective), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
    public ResponseEntity<String> deleteBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @PathVariable @NonNull Number objectiveId

    )
            throws Exception {
        BusinessUnitObjective businessUnitObjective =
                Helper.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        businessUnitObjectiveRepository.deleteById(objectiveId.longValue());
        return new ResponseEntity<>(businessUnitObjective.getTitle() + " deleted", HttpStatus.OK);
    }
}