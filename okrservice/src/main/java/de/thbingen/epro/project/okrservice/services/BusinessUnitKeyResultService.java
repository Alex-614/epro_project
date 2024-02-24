package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface BusinessUnitKeyResultService extends KeyResultService<BusinessUnitKeyResult, BusinessUnitKeyResultDto> {



    BusinessUnitKeyResultDto createKeyResult(long companyId, long businessUnitId, long objectiveId, BusinessUnitKeyResultDto keyResultDto) throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException, BusinessUnitNotFoundException;
    
    BusinessUnitKeyResultDto patchKeyResult(long companyId, long businessUnitId, long keyResultId, KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto) throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException;
    




}
