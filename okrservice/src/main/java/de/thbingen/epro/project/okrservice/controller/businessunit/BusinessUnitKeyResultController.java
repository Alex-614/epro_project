package de.thbingen.epro.project.okrservice.controller.businessunit;

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

import de.thbingen.epro.project.okrservice.controller.Utils;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
public class BusinessUnitKeyResultController {



    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;

    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    
    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;


    @Autowired
    public BusinessUnitKeyResultController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, 
                                            BusinessUnitObjectiveRepository businessUnitObjectiveRepository, 
                                            BusinessUnitKeyResultRepository businessUnitKeyResultRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
    }





    @PostMapping
    public ResponseEntity<KeyResultDto> createBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody @Valid BusinessUnitKeyResultDto keyResultDto) throws Exception {
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
    


    @GetMapping("{keyResultId}")
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


    @GetMapping
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



    @PatchMapping("{keyResultId}")
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



    @DeleteMapping("{keyResultId}")
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
