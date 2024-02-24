package de.thbingen.epro.project.okrservice.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.BusinessUnitObjectiveService;
import de.thbingen.epro.project.okrservice.services.CompanyKeyResultService;
import de.thbingen.epro.project.okrservice.services.CompanyObjectiveService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class CompanyKeyResultServiceImpl extends KeyResultServiceImpl<CompanyKeyResult, CompanyKeyResultDto> implements CompanyKeyResultService {

    private CompanyKeyResultRepository companyKeyResultRepository;
    private BusinessUnitObjectiveService businessUnitObjectiveService;
    private CompanyObjectiveService companyObjectiveService;


    @Autowired
    public CompanyKeyResultServiceImpl(KeyResultTypeRepository keyResultTypeRepository, UserService userService, 
                                        CompanyKeyResultRepository companyKeyResultRepository, CompanyObjectiveService companyObjectiveService, 
                                        BusinessUnitObjectiveService businessUnitObjectiveService, KeyResultUpdateRepository keyResultUpdateRepository) {
        super(keyResultTypeRepository, userService, companyKeyResultRepository,
            keyResultUpdateRepository, (ObjectiveServiceImpl<CompanyObjective, CompanyObjectiveDto>) companyObjectiveService);
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.companyObjectiveService = companyObjectiveService;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
    }



    @Override
    public CompanyKeyResultDto createKeyResult(long companyId, long objectiveId, CompanyKeyResultDto keyResultDto)
            throws MaxKeyResultsReachedException, CompanyNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException {
        CompanyObjective objective = companyObjectiveService.findObjective(objectiveId);
        CompanyKeyResult companyKeyResult = new CompanyKeyResult();
        patchKeyResult(companyKeyResult, keyResultDto);
        companyKeyResult.setObjective(objective);
        List<BusinessUnitObjective> businessUnitObjectives = new ArrayList<>();
        for (Long id : keyResultDto.getRepresenters()) {
            businessUnitObjectives.add(businessUnitObjectiveService.findObjective(id));
        }
        companyKeyResult.setRepresenters(businessUnitObjectives);
        
        companyKeyResultRepository.save(companyKeyResult);
        return companyKeyResult.toDto();
    }

    

    @Override
    public CompanyKeyResultDto patchKeyResult(long companyId, long keyResultId, KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto)
            throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException {

        CompanyKeyResult keyResult = findKeyResult(keyResultId);

        // create copy
        CompanyKeyResult keyResultCopy = new CompanyKeyResult();
        patchKeyResult(keyResultCopy, keyResult.toDto());
        keyResultCopy.setLastUpdate(null);
        keyResultCopy.setRepresenters(keyResult.getRepresenters().stream().collect(Collectors.toList())); // create copy

        // change original
        CompanyKeyResultDto keyResultDto = keyResultPatchDto.getKeyResultDto();
        patchKeyResult(keyResult, keyResultDto);
        if (keyResultDto.getRepresenters() != null) {
            List<BusinessUnitObjective> businessUnitObjectives = new ArrayList<>();
            for (Long id : keyResultDto.getRepresenters()) {
                businessUnitObjectives.add(businessUnitObjectiveService.findObjective(id));
            }
            keyResult.setRepresenters(businessUnitObjectives);
        }

        keyResultCopy = companyKeyResultRepository.save(keyResultCopy);

        // persist the update
        updateKeyResultUpdateHistory(keyResult, keyResultCopy, keyResult, keyResultPatchDto);

        // persist change
        companyKeyResultRepository.save(keyResult);

        return keyResult.toDto();
    }



















}
