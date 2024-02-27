package de.thbingen.epro.project.okrservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultDeprecatedException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.CompanyKeyResultService;
import de.thbingen.epro.project.okrservice.services.CompanyObjectiveService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class CompanyKeyResultServiceImpl extends KeyResultServiceImpl<CompanyKeyResult, CompanyKeyResultDto> implements CompanyKeyResultService {

    private CompanyKeyResultRepository companyKeyResultRepository;
    private CompanyObjectiveService companyObjectiveService;
    private KeyResultUpdateRepository keyResultUpdateRepository;



    @Autowired
    public CompanyKeyResultServiceImpl(KeyResultTypeRepository keyResultTypeRepository, UserService userService, 
                                        CompanyKeyResultRepository companyKeyResultRepository, CompanyObjectiveService companyObjectiveService, 
                                        KeyResultUpdateRepository keyResultUpdateRepository) {
        super(keyResultTypeRepository, userService, companyKeyResultRepository,
            keyResultUpdateRepository, (ObjectiveServiceImpl<CompanyObjective, CompanyObjectiveDto>) companyObjectiveService);
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.companyObjectiveService = companyObjectiveService;
        this.keyResultUpdateRepository = keyResultUpdateRepository;
    }



    @Override
    protected void patchKeyResult(CompanyKeyResult keyResult, CompanyKeyResultDto keyResultDto) throws KeyResultTypeNotFoundException, ObjectiveNotFoundException {
        super.patchKeyResult(keyResult, keyResultDto);
    }


    @Override
    public CompanyKeyResultDto createKeyResult(long objectiveId, CompanyKeyResultDto keyResultDto)
            throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException {
        if (companyObjectiveService.findObjective(objectiveId).getKeyReslts().size() >= 5) {
            throw new MaxKeyResultsReachedException();
        }
        CompanyKeyResult companyKeyResult = new CompanyKeyResult();
        keyResultDto.setObjectiveId(objectiveId);
        patchKeyResult(companyKeyResult, keyResultDto);
        

        companyKeyResultRepository.save(companyKeyResult);
        return companyKeyResult.toDto();
    }

    
    @Override
    public CompanyKeyResultDto patchKeyResult(long keyResultId, KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto)
            throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException, KeyResultDeprecatedException {
        
        if (keyResultUpdateRepository.existsByOldKeyResultId(keyResultId)) {
            throw new KeyResultDeprecatedException();
        }

        CompanyKeyResult keyResult = findKeyResult(keyResultId);

        // create copy
        CompanyKeyResult keyResultCopy = new CompanyKeyResult();
        patchKeyResult(keyResultCopy, keyResult.toDto());
        keyResultCopy.setLastUpdate(null);
        keyResultCopy.setRepresenters(null);

        // change original
        patchKeyResult(keyResultCopy, keyResultPatchDto.getKeyResultDto());

        keyResultCopy = companyKeyResultRepository.save(keyResultCopy);

        // persist the update
        updateKeyResultUpdateHistory(keyResult, keyResultCopy, keyResult, keyResultPatchDto);

        // persist change
        companyKeyResultRepository.save(keyResult);

        return keyResult.toDto();
    }



















}
