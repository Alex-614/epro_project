package de.thbingen.epro.project.okrservice.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.dtos.*;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.repositories.*;
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
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxCompanyObjectivesReachedException;
import jakarta.validation.Valid;

@RestController
public class BusinessUnitController {

    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;

    private UserRepository userRepository;


    @Autowired
    public BusinessUnitController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, 
                                  BusinessUnitObjectiveRepository businessUnitObjectiveRepository,
                                  UserRepository userRepository, BusinessUnitKeyResultRepository businessUnitKeyResultRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.userRepository = userRepository;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
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
        BusinessUnit oldBusinessUnit = Utils.getBusinessUnitFromRepository(companyRepository, companyId,
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
    public ResponseEntity<List<BusinessUnitDto>> getAllBusinessUnit(@PathVariable @NonNull Number companyId){
        List<BusinessUnit> businessUnits = businessUnitRepository.findByCompanyId(companyId.longValue());
        return new ResponseEntity<>(businessUnits.stream()
                .map(BusinessUnitDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }




    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}")
    public ResponseEntity<BusinessUnitDto> getBusinessUnit(@PathVariable @NonNull Number companyId,
                                              @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = Utils.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        return new ResponseEntity<>(new BusinessUnitDto(businessUnit), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/businessunit/{businessUnitId}")
    public ResponseEntity<String> deleteBusinessUnit(@PathVariable @NonNull Number companyId,
                                           @PathVariable @NonNull Number businessUnitId)
            throws Exception {
        BusinessUnit businessUnit = Utils.getBusinessUnitFromRepository(companyRepository, companyId,
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
        BusinessUnit businessUnit = Utils.getBusinessUnitFromRepository(companyRepository, companyId,
                businessUnitRepository, businessUnitId);
        User owner = Utils.getUserFromRepository(userRepository, objectiveDto.getOwnerId());
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
        objective.setRepresented(new ArrayList<CompanyKeyResult>());

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
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        BusinessUnitObjectiveDto oldObjectiveDto = new BusinessUnitObjectiveDto(oldObjective);
        User owner = Utils.getUserFromRepository(userRepository, objectiveDto.getOwnerId());


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
    public ResponseEntity<List<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(
            @PathVariable @NonNull Number companyId,
            @PathVariable @NonNull Number businessUnitId){
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        List<BusinessUnitObjective> businessUnitsObjectives =
                businessUnitObjectiveRepository.findByBusinessUnitId(businessUnitIdObject);
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
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
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
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        businessUnitObjectiveRepository.deleteById(objectiveId.longValue());
        return new ResponseEntity<>(businessUnitObjective.getTitle() + " deleted", HttpStatus.OK);
    }

    @PostMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
    public ResponseEntity<KeyResultDto> createBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody @Valid KeyResultDto keyResultDto) throws Exception {
        BusinessUnitObjective objective =
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        KeyResultType keyResultType = new KeyResultType(keyResultDto.getType());
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult();
        businessUnitKeyResult.setGoal(keyResultDto.getGoal());
        businessUnitKeyResult.setTitle(keyResultDto.getTitle());
        businessUnitKeyResult.setDescription(keyResultDto.getDescription());
        businessUnitKeyResult.setCurrent(keyResultDto.getCurrent());
        businessUnitKeyResult.setConfidenceLevel(keyResultDto.getConfidenceLevel());
        businessUnitKeyResult.setObjective(objective);
        businessUnitKeyResult.setType(keyResultType);

        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        keyResultDto.setId(businessUnitKeyResult.getId());
        return new ResponseEntity<>(keyResultDto, HttpStatus.OK);
    }

    @PatchMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}")
    public ResponseEntity<KeyResultDto> patchBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @PathVariable @NonNull Number keyResultId,
                                                                    @RequestBody BusinessUnitKeyResultDto keyResultDto)
            throws Exception {
        BusinessUnitKeyResult oldBusinessUnitKeyResult =
                Utils.getBusinessUnitKeyResultFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId, businessUnitKeyResultRepository,
                        keyResultId);
        BusinessUnitKeyResultDto oldKeyResultDto =
                new BusinessUnitKeyResultDto(oldBusinessUnitKeyResult);

        Field[] fields = KeyResultDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(keyResultDto);
            if(value != null) {
                field.set(oldKeyResultDto, value);
            }
            field.setAccessible(false);
        }
        fields = BusinessUnitKeyResultDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(keyResultDto);
            if(value != null) {
                field.set(oldKeyResultDto, value);
            }
            field.setAccessible(false);
        }

        BusinessUnitObjective objective =
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        KeyResultType keyResultType = new KeyResultType(oldKeyResultDto.getType());
        oldBusinessUnitKeyResult.setGoal(oldKeyResultDto.getGoal());
        oldBusinessUnitKeyResult.setTitle(oldKeyResultDto.getTitle());
        oldBusinessUnitKeyResult.setDescription(oldKeyResultDto.getDescription());
        oldBusinessUnitKeyResult.setCurrent(oldKeyResultDto.getCurrent());
        oldBusinessUnitKeyResult.setConfidenceLevel(oldKeyResultDto.getConfidenceLevel());
        oldBusinessUnitKeyResult.setObjective(objective);
        oldBusinessUnitKeyResult.setType(keyResultType);

        businessUnitKeyResultRepository.save(oldBusinessUnitKeyResult);
        return new ResponseEntity<>(oldKeyResultDto, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
    public ResponseEntity<List<BusinessUnitKeyResultDto>> getAllBusinessUnitKeyResults(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId)
        throws Exception {
        List<BusinessUnitKeyResult> businessUnitKeyResults =
                businessUnitKeyResultRepository.findAllByObjectiveId(objectiveId.longValue());
        return new ResponseEntity<>(businessUnitKeyResults.stream()
                .map(BusinessUnitKeyResultDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}")
    public ResponseEntity<BusinessUnitKeyResultDto> getBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                                       @PathVariable @NonNull Number businessUnitId,
                                                                                       @PathVariable @NonNull Number objectiveId,
                                                                                   @PathVariable @NonNull Number keyResultId)
            throws Exception {
        BusinessUnitKeyResult businessUnitKeyResult =
                Utils.getBusinessUnitKeyResultFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId, businessUnitKeyResultRepository,
                        keyResultId);
        return new ResponseEntity<>(new BusinessUnitKeyResultDto(businessUnitKeyResult), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}")
    public ResponseEntity<BusinessUnitKeyResultDto> deleteBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId,
                                                                             @PathVariable @NonNull Number keyResultId)
            throws Exception {
        BusinessUnitKeyResult businessUnitKeyResult =
                Utils.getBusinessUnitKeyResultFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId, businessUnitKeyResultRepository,
                        keyResultId);
        businessUnitKeyResultRepository.deleteById(businessUnitKeyResult.getId());
        return new ResponseEntity<>(new BusinessUnitKeyResultDto(businessUnitKeyResult), HttpStatus.OK);
    }
}