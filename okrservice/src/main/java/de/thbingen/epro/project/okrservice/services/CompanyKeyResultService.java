package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface CompanyKeyResultService extends KeyResultService<CompanyKeyResult, CompanyKeyResultDto> {


    /**
     * 
     * Creates a new CompanyKeyResult. 
     * 
     * @param objectiveId used to search for the Objective to create the subordinate KeyResult in
     * @param keyResultDto contains the data to create the CompanyKeyResult from
     * @return the corresponding CompanyKeyResultDto
     * @throws MaxKeyResultsReachedException if the CompanyObjective already has 5 subordinate KeyResults
     * @throws CompanyNotFoundException
     * @throws UserNotFoundException 
     * @throws ObjectiveNotFoundException 
     * @throws KeyResultTypeNotFoundException 
     */
    CompanyKeyResultDto createKeyResult(long objectiveId, CompanyKeyResultDto keyResultDto) 
        throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException;
    


    /**
     * 
     * Changes a CompanyKeyResult and persists. 
     * Only valid values will be changed.
     * 
     * @param keyResultId used to search for the KeyResult
     * @param keyResultPatchDto contains the changed data
     * @return 
     * @throws KeyResultNotFoundException if the KeyResult was not found
     * @throws UserNotFoundException if the updater was not found
     * @throws ObjectiveNotFoundException if the related Objective was not found
     * @throws KeyResultTypeNotFoundException if the Type was not found
     */
    CompanyKeyResultDto patchKeyResult(long keyResultId, KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto) 
        throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException;
    

}
