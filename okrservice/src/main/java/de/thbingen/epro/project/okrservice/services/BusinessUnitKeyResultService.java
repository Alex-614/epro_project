package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultDeprecatedException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface BusinessUnitKeyResultService extends KeyResultService<BusinessUnitKeyResult, BusinessUnitKeyResultDto> {


    /**
     * 
     * Creates a new BusinessUnitKeyResult. 
     * 
     * @param objectiveId used to search for the Objective to create the subordinate KeyResult in
     * @param keyResultDto contains the data to create the BusinessUnitKeyResult from
     * @return the corresponding BusinessUnitKeyResultDto
     * @throws MaxKeyResultsReachedException if the BusinessUnitObjective already has 5 subordinate KeyResults
     * @throws CompanyNotFoundException
     * @throws UserNotFoundException 
     * @throws ObjectiveNotFoundException 
     * @throws KeyResultTypeNotFoundException 
     */
    BusinessUnitKeyResultDto createKeyResult(long companyId, long businessUnitId, long objectiveId, BusinessUnitKeyResultDto keyResultDto) 
        throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException, BusinessUnitNotFoundException;
    


    /**
     * 
     * Changes a BusinessUnitKeyResult and persists. 
     * Only valid values will be changed.
     * 
     * @param keyResultId used to search for the KeyResult
     * @param keyResultPatchDto contains the changed data
     * @return 
     * @throws KeyResultNotFoundException if the KeyResult was not found
     * @throws UserNotFoundException if the updater was not found
     * @throws ObjectiveNotFoundException if the related Objective was not found
     * @throws KeyResultTypeNotFoundException if the Type was not found
     * @throws KeyResultDeprecatedException 
     */
    BusinessUnitKeyResultDto patchKeyResult(long keyResultId, KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto) 
        throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException, KeyResultDeprecatedException;
    




}
