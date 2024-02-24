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

    CompanyKeyResultDto createKeyResult(long companyId, long objectiveId, CompanyKeyResultDto keyResultDto) throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException;
    
    CompanyKeyResultDto patchKeyResult(long companyId, long keyResultId, KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto) throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException;
    

}
