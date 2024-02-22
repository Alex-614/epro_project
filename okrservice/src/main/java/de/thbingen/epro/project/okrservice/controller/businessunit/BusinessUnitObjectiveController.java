package de.thbingen.epro.project.okrservice.controller.businessunit;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.MaxCompanyObjectivesReachedException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective")
public class BusinessUnitObjectiveController {

    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    private UserRepository userRepository;


    @Autowired
    public BusinessUnitObjectiveController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, 
                                    BusinessUnitObjectiveRepository businessUnitObjectiveRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.userRepository = userRepository;
    }






    @PostMapping
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

    @GetMapping
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

    @GetMapping("{objectiveId}")
    public ResponseEntity<BusinessUnitObjectiveDto> getBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId
    ) throws Exception {
        BusinessUnitObjective businessUnitObjective =
                Utils.getBusinessUnitObjectiveFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId);
        return new ResponseEntity<>(new BusinessUnitObjectiveDto(businessUnitObjective), HttpStatus.OK);
    }

    @PatchMapping("{objectiveId}")
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
    
    @DeleteMapping("{objectiveId}")
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















}
