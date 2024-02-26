package de.thbingen.epro.project.okrservice.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultTypeNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxKeyResultsReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.BusinessUnitKeyResultService;
import de.thbingen.epro.project.okrservice.services.BusinessUnitObjectiveService;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class BusinessUnitKeyResultServiceImpl extends KeyResultServiceImpl<BusinessUnitKeyResult, BusinessUnitKeyResultDto> implements BusinessUnitKeyResultService {



    private BusinessUnitService businessUnitService;
    private BusinessUnitObjectiveService businessUnitObjectiveService;
    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;

    @Autowired
    public BusinessUnitKeyResultServiceImpl(KeyResultTypeRepository keyResultTypeRepository, UserService userService,
            KeyResultRepository<BusinessUnitKeyResult> keyResultRepository, BusinessUnitService businessUnitService, 
            KeyResultUpdateRepository keyResultUpdateRepository, BusinessUnitObjectiveService businessUnitObjectiveService, 
            BusinessUnitKeyResultRepository businessUnitKeyResultRepository) {
        super(keyResultTypeRepository, userService, keyResultRepository, keyResultUpdateRepository, 
            (ObjectiveServiceImpl<? extends Objective, ? extends ObjectiveDto>) businessUnitObjectiveService);
        this.businessUnitService = businessUnitService;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
    }

    @Override
    public BusinessUnitKeyResultDto createKeyResult(long companyId, long businessUnitId, long objectiveId,
            BusinessUnitKeyResultDto keyResultDto) throws MaxKeyResultsReachedException, CompanyNotFoundException,
            UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException, BusinessUnitNotFoundException {
        System.out.println(keyResultDto.getContributingBusinessUnits());

        BusinessUnitObjective objective = businessUnitObjectiveService.findObjective(objectiveId);
        
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult();
        patchKeyResult(businessUnitKeyResult, keyResultDto);
        List<BusinessUnit> bus = Collections.singletonList(businessUnitService.findBusinessUnit(companyId, businessUnitId));
        bus.addAll(keyResultDto.getContributingBusinessUnits().stream().map(BusinessUnit::new).collect(Collectors.toList()));
        businessUnitKeyResult.setContributingBusinessUnits(bus);
        businessUnitKeyResult.setObjective(objective);

        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        return businessUnitKeyResult.toDto();
    }



    @Override
    public BusinessUnitKeyResultDto patchKeyResult(long keyResultId,KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto) 
            throws KeyResultNotFoundException, UserNotFoundException, ObjectiveNotFoundException, KeyResultTypeNotFoundException {
                
        BusinessUnitKeyResult keyResult = findKeyResult(keyResultId);

        // create copy
        BusinessUnitKeyResult keyResultCopy = new BusinessUnitKeyResult();
        patchKeyResult(keyResultCopy, keyResult.toDto());
        keyResultCopy.setLastUpdate(null);

        // change original
        BusinessUnitKeyResultDto keyResultDto = keyResultPatchDto.getKeyResultDto();
        patchKeyResult(keyResult, keyResultDto);

        keyResultCopy = businessUnitKeyResultRepository.save(keyResultCopy);

        // persist the update
        updateKeyResultUpdateHistory(keyResult, keyResultCopy, keyResult, keyResultPatchDto);

        // persist change
        businessUnitKeyResultRepository.save(keyResult);

        return keyResult.toDto();
    }

}
