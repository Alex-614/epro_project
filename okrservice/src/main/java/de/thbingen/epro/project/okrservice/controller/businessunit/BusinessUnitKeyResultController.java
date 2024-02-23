package de.thbingen.epro.project.okrservice.controller.businessunit;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
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
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
public class BusinessUnitKeyResultController {

    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private UserRepository userRepository;

    private KeyResultTypeRepository keyResultTypeRepository;
    private KeyResultUpdateRepository keyResultUpdateRepository;
    private Utils utils;

    @Autowired
    public BusinessUnitKeyResultController( BusinessUnitKeyResultRepository businessUnitKeyResultRepository, UserRepository userRepository, 
                                            KeyResultTypeRepository keyResultTypeRepository, KeyResultUpdateRepository keyResultUpdateRepository, Utils utils) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.userRepository = userRepository;
        this.keyResultTypeRepository = keyResultTypeRepository;
        this.keyResultUpdateRepository = keyResultUpdateRepository;
        this.utils = utils;
    }





    @PostMapping
    public ResponseEntity<KeyResultDto> createBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody @Valid BusinessUnitKeyResultDto keyResultDto) throws Exception {
        BusinessUnitObjective objective =
        utils.getBusinessUnitObjectiveFromRepository(companyId, businessUnitId, objectiveId);
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
        BusinessUnitKeyResult businessUnitKeyResult = utils.getBusinessUnitKeyResultFromRepository(companyId, businessUnitId, objectiveId, keyResultId);
        return new ResponseEntity<>(new BusinessUnitKeyResultDto(businessUnitKeyResult), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<BusinessUnitKeyResultDto>> getAllBusinessUnitKeyResults(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId)
        throws Exception {
        List<BusinessUnitKeyResult> businessUnitKeyResults = businessUnitKeyResultRepository.findAllByObjectiveId(objectiveId.longValue());
        return new ResponseEntity<>(businessUnitKeyResults.stream()
                .map(BusinessUnitKeyResultDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("{keyResultId}/updatehistory")
    public ResponseEntity<LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>>> getBusinessUnitKeyResultUpdateHistory(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        BusinessUnitKeyResult businessUnitKeyResultKeyResult = utils.getBusinessUnitKeyResultFromRepository(companyId, businessUnitId, objectiveId, keyResultId);
        LinkedList<KeyResultUpdate> updateHistory = new LinkedList<>();
        if (businessUnitKeyResultKeyResult.getLastUpdate() != null) {
            updateHistory.add(businessUnitKeyResultKeyResult.getLastUpdate());
            while (updateHistory.getLast().getOldKeyResult().getLastUpdate() != null) {
                updateHistory.add(updateHistory.getLast().getOldKeyResult().getLastUpdate());
            }
        }
        LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>> updateHistoryDto = new LinkedList<>();
        for (KeyResultUpdate update : updateHistory) {
            BusinessUnitKeyResult newCKR = businessUnitKeyResultRepository.findById(update.getNewKeyResult().getId().longValue()).get();
            BusinessUnitKeyResult oldCKR = businessUnitKeyResultRepository.findById(update.getOldKeyResult().getId().longValue()).get();
            BusinessUnitKeyResult CKR = businessUnitKeyResultRepository.findById(update.getKeyResult().getId().longValue()).get();
            KeyResultUpdateDto<BusinessUnitKeyResultDto> updateDto = new KeyResultUpdateDto<BusinessUnitKeyResultDto>(
                        new KeyResultPatchDto<BusinessUnitKeyResultDto>(update.getStatusUpdate(), update.getUpdateTimestamp().toEpochMilli(), new BusinessUnitKeyResultDto(newCKR)), 
                        new BusinessUnitKeyResultDto(oldCKR), new BusinessUnitKeyResultDto(CKR));
            updateHistoryDto.add(updateDto);
        }

        return new ResponseEntity<>(updateHistoryDto, HttpStatus.OK);
    }

    @PatchMapping("{keyResultId}")
    public ResponseEntity<KeyResultDto> patchBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @PathVariable @NonNull Number keyResultId,
                                                                    @RequestBody @Valid KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto)
            throws Exception {
        BusinessUnitKeyResultDto keyResultDto = keyResultPatchDto.getKeyResultDto();
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new Exception();
        }

        BusinessUnitKeyResult keyResult = utils.getBusinessUnitKeyResultFromRepository(companyId, businessUnitId, objectiveId, keyResultId);

        /*
         * Update History
         */

        BusinessUnitKeyResult keyResultCopy = new BusinessUnitKeyResult();
        keyResultCopy.setConfidenceLevel(keyResult.getConfidenceLevel());
        keyResultCopy.setCurrent(keyResult.getCurrent());
        keyResultCopy.setDescription(keyResult.getDescription());
        keyResultCopy.setGoal(keyResult.getGoal());
        keyResultCopy.setLastUpdate(null);
        keyResultCopy.setObjective(keyResult.getObjective());
        keyResultCopy.setTitle(keyResult.getTitle());
        keyResultCopy.setType(keyResult.getType());


        // perform the KeyResult change but dont persist, just validation
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


        keyResultCopy = businessUnitKeyResultRepository.save(keyResultCopy);

        // persist the update
        KeyResultUpdate update = new KeyResultUpdate();
        update.setKeyResult(keyResult);
        update.setStatusUpdate(keyResultPatchDto.getStatusUpdate());
        update.setUpdateTimestamp(Instant.now());
        update.setOldKeyResult(keyResultCopy);
        update.setNewKeyResult(keyResult);
        update.setUpdater(user);
        keyResultUpdateRepository.save(update);
        
        // correct the last update
        // store the last Update for correction
        KeyResultUpdate lastUpdate = keyResult.getLastUpdate();
        if (lastUpdate != null) {
            lastUpdate.setNewKeyResult(keyResultCopy);
            keyResultUpdateRepository.save(lastUpdate);
        }

        // persist change
        businessUnitKeyResultRepository.save(keyResult);
        
        return new ResponseEntity<>(new BusinessUnitKeyResultDto(keyResult), HttpStatus.OK);
    }



    @DeleteMapping("{keyResultId}")
    public ResponseEntity<Void> deleteBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId,
                                                                             @PathVariable @NonNull Number keyResultId)
            throws Exception {
        BusinessUnitKeyResult businessUnitKeyResult = utils.getBusinessUnitKeyResultFromRepository(companyId, businessUnitId, objectiveId, keyResultId);
        businessUnitKeyResultRepository.deleteById(businessUnitKeyResult.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    
}
