package de.thbingen.epro.project.okrservice.controller.businessunit;

import java.util.List;
import java.util.Optional;
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
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
public class BusinessUnitKeyResultController {



    private CompanyRepository companyRepository;

    private BusinessUnitRepository businessUnitRepository;

    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    
    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;

    private KeyResultTypeRepository keyResultTypeRepository;

    @Autowired
    public BusinessUnitKeyResultController(CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, 
                                            BusinessUnitObjectiveRepository businessUnitObjectiveRepository, 
                                            BusinessUnitKeyResultRepository businessUnitKeyResultRepository, 
                                            KeyResultTypeRepository keyResultTypeRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.keyResultTypeRepository = keyResultTypeRepository;
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
        BusinessUnitKeyResult keyResult =
                Utils.getBusinessUnitKeyResultFromRepository(companyRepository, companyId, businessUnitRepository,
                        businessUnitId, businessUnitObjectiveRepository, objectiveId, businessUnitKeyResultRepository,
                        keyResultId);

        Optional<KeyResultType> keyResultType = null;
        if (keyResultDto.getType() != null) {
            keyResultType = keyResultTypeRepository.findByName(keyResultDto.getType());
            if (!keyResultType.isPresent()) {
                throw new KeyResultNotFoundException();
            }
        }
        if (keyResultDto.getGoal() != null) keyResult.setGoal(keyResultDto.getGoal());
        if (keyResultDto.getTitle() != null) keyResult.setTitle(keyResultDto.getTitle());
        if (keyResultDto.getDescription() != null) keyResult.setDescription(keyResultDto.getDescription());
        if (keyResultDto.getCurrent() != null) keyResult.setCurrent(keyResultDto.getCurrent());
        if (keyResultDto.getConfidenceLevel() != null) keyResult.setConfidenceLevel(keyResultDto.getConfidenceLevel());
        if (keyResultType != null) keyResult.setType(keyResultType.get());

        businessUnitKeyResultRepository.save(keyResult);
        return new ResponseEntity<>(new BusinessUnitKeyResultDto(keyResult), HttpStatus.OK);
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
