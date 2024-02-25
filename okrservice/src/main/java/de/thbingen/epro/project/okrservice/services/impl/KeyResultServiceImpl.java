package de.thbingen.epro.project.okrservice.services.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.dtos.UpdateDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.KeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.KeyResultService;
import de.thbingen.epro.project.okrservice.services.UserService;

/**
 * The `KeyResultServiceImpl` class is an abstract implementation of a service for managing KeyResults
 * the OKR (Objectives and Key Results) system.
 */
@Service
public abstract class KeyResultServiceImpl<T extends KeyResult, K extends KeyResultDto> implements KeyResultService<T, K> {

    private UserService userService;

    private KeyResultTypeRepository keyResultTypeRepository;
    private KeyResultUpdateRepository keyResultUpdateRepository;
    private KeyResultRepository<T> keyResultRepository;
    private ObjectiveServiceImpl<? extends Objective, ? extends ObjectiveDto> objectiveService;

    @Autowired
    public KeyResultServiceImpl(KeyResultTypeRepository keyResultTypeRepository, UserService userService, KeyResultRepository<T> keyResultRepository, 
                            KeyResultUpdateRepository keyResultUpdateRepository, ObjectiveServiceImpl<? extends Objective, ? extends ObjectiveDto> objectiveService) {
        this.keyResultTypeRepository = keyResultTypeRepository;
        this.keyResultUpdateRepository = keyResultUpdateRepository;
        this.keyResultRepository = keyResultRepository;
        this.objectiveService = objectiveService;
        this.userService = userService;
    }
    
    /**
     * Updates a KeyResult object based on the provided KeyResultDto.
     * The Change is NOT persisted.
     * Only NonNull values are set.
     * 
     * @param keyResult the KeyResult that will be updated.
     * @param keyResultDto the DTO, of which the values are retrieved from 
     */
    protected void patchKeyResult(T keyResult, K keyResultDto) throws KeyResultTypeNotFoundException, ObjectiveNotFoundException { 
        if (keyResultDto.getType() != null && !keyResultDto.getType().trim().isEmpty()) {
            KeyResultType type = keyResultTypeRepository.findByName(keyResultDto.getType()).orElseThrow(() -> new KeyResultTypeNotFoundException());
            keyResult.setType(type);
        }
        if (keyResultDto.getObjectiveId() != null)
            keyResult.setObjective(objectiveService.findObjective(keyResultDto.getObjectiveId()));
        if (keyResultDto.getGoal() != null) 
            keyResult.setGoal(keyResultDto.getGoal());
        if (keyResultDto.getTitle() != null) 
            keyResult.setTitle(keyResultDto.getTitle());
        if (keyResultDto.getDescription() != null) 
            keyResult.setDescription(keyResultDto.getDescription());
        if (keyResultDto.getCurrent() != null) 
            keyResult.setCurrent(keyResultDto.getCurrent());
        if (keyResultDto.getConfidenceLevel() != null) 
            keyResult.setConfidenceLevel(keyResultDto.getConfidenceLevel());
        if (keyResultDto.getContributingUnits() != null) 
            keyResult.setContributingUnits(keyResultDto.getContributingUnits().stream().map(Unit::new).collect(Collectors.toList()));
        if (keyResultDto.getContributingBusinessUnits() != null) 
            keyResult.setContributingBusinessUnits(keyResultDto.getContributingBusinessUnits().stream().map(BusinessUnit::new).collect(Collectors.toList()));
    }


    /**
     * 
     * <h3>Creates a new KeyResultUpdate and persists it</h3>
     * 
     * <h4>To properly perform an Update/Patch to a KeyResult:</h4>
     * <ol>
     *      <li>create a deep copy of the current KeyResult (note to set the lastUpdate to null)</li>
     *      <li>update the original KeyResult</li>
     *      <li>persist the copy</li>
     *      <li>call this method and set the keyResult & newKeyResult to the Original and the oldKeyResult should be the copy</li>
     *      <li>persist the original KeyResult</li>
     * </ol>
     * 
     * @param keyResult is the current key result that needs to be updated.
     * @param oldKeyResult represents the previous state of a key result before it was updated.
     * @param newKeyResult represents the updated key result.
     * @param updateDto contains information related to the update being performed.
     * @throws UserNotFoundException if the updater, set in the updateDto, is not found
     */
    protected void updateKeyResultUpdateHistory(T keyResult, T oldKeyResult, T newKeyResult, UpdateDto updateDto) throws UserNotFoundException {
        KeyResultUpdate update = new KeyResultUpdate();
        update.setKeyResult(keyResult);
        update.setStatusUpdate(updateDto.getStatusUpdate());
        update.setOldKeyResult(oldKeyResult);
        update.setNewKeyResult(keyResult);
        User updater = userService.findUser(updateDto.getUpdaterId());
        update.setUpdater(updater);
        KeyResultUpdate lastUpdate = keyResult.getLastUpdate();
        if (lastUpdate != null) {
            lastUpdate.setNewKeyResult(update.getOldKeyResult());
            keyResultUpdateRepository.save(lastUpdate);
        }
        keyResultUpdateRepository.save(update);
    }

    
    
    @Override
    public T findKeyResult(long keyResultId) throws KeyResultNotFoundException {
        T companyKeyResult = keyResultRepository.findById(keyResultId).orElseThrow(() -> new KeyResultNotFoundException());
        return companyKeyResult;
    }
    
    @Override
    public List<K> findAllKeyResults(long objectiveId) {
        List<T> keyResults = keyResultRepository.findByObjectiveId(objectiveId);
        List<K> result = new ArrayList<>();
        for (T k : keyResults) {
            result.add(k.toDto());
        }
        return result;
    }



    @Override
    public List<BusinessUnitDto> findContributingBusinessUnits(long keyResultId) throws KeyResultNotFoundException {
        return findKeyResult(keyResultId).getContributingBusinessUnits().stream().map(m -> m.toDto()).collect(Collectors.toList());
    }
    
    

    @Override
    public List<UnitDto> findContributingUnits(long keyResultId) throws KeyResultNotFoundException {
        return findKeyResult(keyResultId).getContributingUnits().stream().map(m -> m.toDto()).collect(Collectors.toList());
    }


    @Override
    public LinkedList<KeyResultUpdateDto<K>> findKeyResultUpdateHistory(long keyResultId) throws KeyResultNotFoundException {
        T keyResult = findKeyResult(keyResultId);
        LinkedList<KeyResultUpdate> updateHistory = new LinkedList<>();
        if (keyResult.getLastUpdate() != null) {
            updateHistory.add(keyResult.getLastUpdate());
            while (updateHistory.getLast().getOldKeyResult().getLastUpdate() != null) {
                updateHistory.add(updateHistory.getLast().getOldKeyResult().getLastUpdate());
            }
        }
        LinkedList<KeyResultUpdateDto<K>> updateHistoryDto = new LinkedList<>();
        for (KeyResultUpdate update : updateHistory) {
            T newCKR = keyResultRepository.findById(update.getNewKeyResult().getId().longValue()).get();
            T oldCKR = keyResultRepository.findById(update.getOldKeyResult().getId().longValue()).get();
            T CKR = keyResultRepository.findById(update.getKeyResult().getId().longValue()).get();
            KeyResultUpdateDto<K> updateDto = new KeyResultUpdateDto<K>(
                        update.getStatusUpdate(), update.getUpdateTimestamp().toEpochMilli(), update.getUpdater().getId(), newCKR.toDto(), 
                        oldCKR.toDto(), CKR.toDto());
            updateHistoryDto.add(updateDto);
        }
        return updateHistoryDto;        
    }


    @Override
    public void deleteKeyResult(long keyResultId) {
        keyResultRepository.deleteById(keyResultId);
    }



}
