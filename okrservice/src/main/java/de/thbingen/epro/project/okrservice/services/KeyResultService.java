package de.thbingen.epro.project.okrservice.services;

import java.util.LinkedList;
import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;

public interface KeyResultService<T extends KeyResult, K extends KeyResultDto> {


    T findKeyResult(long keyResultId) throws KeyResultNotFoundException;
    List<K> findAllKeyResults(long objectiveId);
    void deleteKeyResult(long keyResultId);


    List<BusinessUnitDto> findContributingBusinessUnits(long keyResultId) throws KeyResultNotFoundException;
    List<UnitDto> findContributingUnits(long keyResultId) throws KeyResultNotFoundException;

    LinkedList<KeyResultUpdateDto<K>> findKeyResultUpdateHistory(long keyResultId) throws KeyResultNotFoundException;

}
