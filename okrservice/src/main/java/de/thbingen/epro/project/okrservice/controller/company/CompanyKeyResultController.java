package de.thbingen.epro.project.okrservice.controller.company;

import java.time.Instant;
import java.util.ArrayList;
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
import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective/{objectiveId}/keyresult")
public class CompanyKeyResultController {





    private CompanyKeyResultRepository companyKeyResultRepository;
    private UserRepository userRepository;

    private KeyResultTypeRepository keyResultTypeRepository;
    private KeyResultUpdateRepository keyResultUpdateRepository;

    private Utils utils;
    private EntityManager entityManager;

    @Autowired
    public CompanyKeyResultController(CompanyKeyResultRepository companyKeyResultRepository, KeyResultTypeRepository keyResultTypeRepository, 
                                        KeyResultUpdateRepository keyResultUpdateRepository, Utils utils, EntityManager entityManager, 
                                        UserRepository userRepository) {
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.userRepository = userRepository;
        this.keyResultTypeRepository = keyResultTypeRepository;
        this.keyResultUpdateRepository = keyResultUpdateRepository;
        this.utils = utils;
        this.entityManager = entityManager;
    }






    @PostMapping
    public ResponseEntity<CompanyKeyResultDto> createCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                                   @PathVariable @NonNull Number objectiveId,
                                                                   @RequestBody @Valid CompanyKeyResultDto keyResultDto) throws Exception {
        Optional<KeyResultType> keyResultType = keyResultTypeRepository.findByName(keyResultDto.getType());
        if (!keyResultType.isPresent()) {
            throw new KeyResultNotFoundException();
        }
        CompanyObjective objective = utils.getCompanyObjectiveFromRepository(companyId, objectiveId);
        
        CompanyKeyResult companyKeyResult = new CompanyKeyResult();
        companyKeyResult.setGoal(keyResultDto.getGoal());
        companyKeyResult.setTitle(keyResultDto.getTitle());
        companyKeyResult.setDescription(keyResultDto.getDescription());
        companyKeyResult.setCurrent(keyResultDto.getCurrent());
        companyKeyResult.setConfidenceLevel(keyResultDto.getConfidenceLevel());
        companyKeyResult.setObjective(objective);
        companyKeyResult.setType(keyResultType.get());

        companyKeyResultRepository.save(companyKeyResult);
        keyResultDto.setId(companyKeyResult.getId());
        return new ResponseEntity<>(keyResultDto, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<CompanyKeyResultDto>> getAllCompanyKeyResults(@PathVariable @NonNull Number objectiveId) {
        List<CompanyKeyResult> companyKeyResults = companyKeyResultRepository.findAllByObjectiveId(objectiveId.longValue());
        return new ResponseEntity<>(companyKeyResults.stream()
                .map(CompanyKeyResultDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> getCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult = utils.getCompanyKeyResultFromRepository(companyId, objectiveId, keyResultId);
        return new ResponseEntity<>(new CompanyKeyResultDto(companyKeyResult), HttpStatus.OK);
    }





    @GetMapping("{keyResultId}/updatehistory")
    public ResponseEntity<LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>>> getCompanyKeyResultUpdateHistory(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult = utils.getCompanyKeyResultFromRepository(companyId, objectiveId, keyResultId);
        LinkedList<KeyResultUpdate> updateHistory = new LinkedList<>();
        if (companyKeyResult.getLastUpdate() != null) {
            updateHistory.add(companyKeyResult.getLastUpdate());
            while (updateHistory.getLast().getOldKeyResult().getLastUpdate() != null) {
                updateHistory.add(updateHistory.getLast().getOldKeyResult().getLastUpdate());
            }
        }
        LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>> updateHistoryDto = new LinkedList<>();
        for (KeyResultUpdate update : updateHistory) {
            CompanyKeyResult newCKR = companyKeyResultRepository.findById(update.getNewKeyResult().getId().longValue()).get();
            CompanyKeyResult oldCKR = companyKeyResultRepository.findById(update.getOldKeyResult().getId().longValue()).get();
            CompanyKeyResult CKR = companyKeyResultRepository.findById(update.getKeyResult().getId().longValue()).get();
            KeyResultUpdateDto<CompanyKeyResultDto> updateDto = new KeyResultUpdateDto<CompanyKeyResultDto>(
                        new KeyResultPatchDto<CompanyKeyResultDto>(update.getStatusUpdate(), update.getUpdateTimestamp().toEpochMilli(), new CompanyKeyResultDto(newCKR)), 
                        new CompanyKeyResultDto(oldCKR), new CompanyKeyResultDto(CKR));
            updateHistoryDto.add(updateDto);
        }

        return new ResponseEntity<>(updateHistoryDto, HttpStatus.OK);
    }




    @PatchMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> patchCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                               @PathVariable @NonNull Number objectiveId,
                                                               @PathVariable @NonNull Number keyResultId,
                                                               @RequestBody @Valid KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto)
            throws Exception {
        if (keyResultUpdateRepository.existsByOldKeyResultId(keyResultId.longValue())) {
            throw new KeyResultNotFoundException();
        }
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new Exception();
        }

        CompanyKeyResult keyResult = utils.getCompanyKeyResultFromRepository(companyId, objectiveId, keyResultId);
        



        /*
         * Update History
         */

        CompanyKeyResult keyResultCopy = new CompanyKeyResult();
        keyResultCopy.setConfidenceLevel(keyResult.getConfidenceLevel());
        keyResultCopy.setCurrent(keyResult.getCurrent());
        keyResultCopy.setDescription(keyResult.getDescription());
        keyResultCopy.setGoal(keyResult.getGoal());
        keyResultCopy.setLastUpdate(null);
        keyResultCopy.setObjective(keyResult.getObjective());
        keyResultCopy.setRepresenters(keyResult.getRepresenters().stream().collect(Collectors.toList()));
        keyResultCopy.setTitle(keyResult.getTitle());
        keyResultCopy.setType(keyResult.getType());



        // perform the KeyResult change but dont persist, just validation
        CompanyKeyResultDto keyResultDto = keyResultPatchDto.getKeyResultDto();
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
        if (keyResultDto.getRepresenters() != null) {
            List<BusinessUnitObjective> businessUnitObjectives = new ArrayList<>();
            for (Long id : keyResultDto.getRepresenters()) {
                businessUnitObjectives.add(utils.getBusinessUnitObjectiveFromRepository(companyId, id, objectiveId));
            }
            keyResult.setRepresenters(businessUnitObjectives);
        }




        keyResultCopy = companyKeyResultRepository.save(keyResultCopy);

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
        companyKeyResultRepository.save(keyResult);
        
        return new ResponseEntity<>(new CompanyKeyResultDto(keyResult), HttpStatus.OK);
    }




    @DeleteMapping("{keyResultId}")
    public ResponseEntity<Void> deleteCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                            @PathVariable @NonNull Number objectiveId,
                                                            @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult = utils.getCompanyKeyResultFromRepository(companyId, objectiveId, keyResultId);
        companyKeyResultRepository.deleteById(companyKeyResult.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }








}
