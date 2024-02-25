package de.thbingen.epro.project.okrservice.services;

import java.util.LinkedList;
import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.KeyResultRepository;

/**
 * This Java interface `KeyResultService` defines a set of methods that can be implemented by classes
 * to provide functionality related to Key Results.
 * 
 * 
 * @see KeyResult
 * @see KeyResultRepository
 * 
 */ 
public interface KeyResultService<T extends KeyResult, K extends KeyResultDto> {


    /**
     * Finds a KeyResult by its ID.
     * 
     * @param keyResultId unique identifier used to search for the KeyResult in the system.
     * @return returns the KeyResult.
     * @throws KeyResultNotFoundException if the KeyResult was not found.
     */
    T findKeyResult(long keyResultId) throws KeyResultNotFoundException;



    /**
     * Retrieves all KeyResult associated with a specific objective and returns them as a list of DTOs.
     * 
     * @param objectiveId is used to specify the ID of the objective for which you want to retrieve KeyResults.
     * @return The method `findAllKeyResults` is returning a list of objects of type `K`. Never returns null.
     */
    List<K> findAllKeyResults(long objectiveId);



    /**
     * Deletes a KeyResult from a repository based on its ID.
     * 
     * If the KeyResult was not found it is silently ignored.
     * 
     * @param keyResultId unique identifier of the key result that needs to be deleted.
     */
    void deleteKeyResult(long keyResultId);



    /**
     * Retrieves a list of BusinessUnitDto objects associated with a specific KeyResult.
     * 
     * @param keyResultId unique identifier used to find a specific KeyResult in the system.
     * @return A list of BusinessUnitDto objects is being returned. Never returns null.
     * @throws KeyResultNotFoundException if the KeyResult was not found
     */
    List<BusinessUnitDto> findContributingBusinessUnits(long keyResultId) throws KeyResultNotFoundException;
    


    /**
     * Retrieves a list of contributing units for a given KeyResultId.
     * 
     * @param keyResultId unique identifier of a KeyResult for which you want to find the contributing units.
     * @return A list of UnitDto objects representing contributing units for a given key result ID.
     * @throws KeyResultNotFoundException if the KeyResult was not found
     */
    List<UnitDto> findContributingUnits(long keyResultId) throws KeyResultNotFoundException;



    /**
     * Retrieves the update history of a KeyResult and converts it into a list of DTO objects.
     * 
     * The method first finds the KeyResult using the `findKeyResult` method, 
     * then iterates through the update history starting from the last update and adds each.
     * 
     * @param keyResultId unique identifier used to find a specific KeyResult in the system.
     * @return returns a `LinkedList` of `KeyResultUpdateDto` objects for a given `keyResultId`.
     * @throws KeyResultNotFoundException if the KeyResult was not found.
     */
    LinkedList<KeyResultUpdateDto<K>> findKeyResultUpdateHistory(long keyResultId) throws KeyResultNotFoundException;



}
