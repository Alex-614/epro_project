package de.thbingen.epro.project.okrservice.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.services.BusinessUnitObjectiveService;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class BusinessUnitObjectiveServiceImpl extends ObjectiveServiceImpl<BusinessUnitObjective, BusinessUnitObjectiveDto> implements BusinessUnitObjectiveService {


    private BusinessUnitService businessUnitService;

    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;


    @Autowired
    public BusinessUnitObjectiveServiceImpl(UserService userService, BusinessUnitService businessUnitService, BusinessUnitObjectiveRepository businessUnitObjectiveRepository) {
        super(userService, businessUnitObjectiveRepository);
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitService = businessUnitService;
    }


    @Override
    public BusinessUnitObjectiveDto createObjective(long companyId, long businessUnitId,
            BusinessUnitObjectiveDto objectiveDto) throws MaxObjectivesReachedException, BusinessUnitNotFoundException, UserNotFoundException {
        BusinessUnit businessUnit = businessUnitService.findBusinessUnit(companyId, businessUnitId);

        // TODO just use SQL Count (new repository method) to not fetch all data 
        if (businessUnit.getObjectives().size() >= 5) {
            throw new MaxObjectivesReachedException();
        }

        BusinessUnitObjective objective = new BusinessUnitObjective();
        patchObjective(objective, objectiveDto);
        objective.setBusinessUnit(businessUnit);
        objective.setRepresented(new ArrayList<CompanyKeyResult>());

        businessUnitObjectiveRepository.save(objective);

        return objective.toDto();
    }


    @Override
    public List<BusinessUnitObjectiveDto> findAllObjectives(long companyId, long businessUnitId) {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId, companyId);
        List<BusinessUnitObjective> businessUnitsObjectives = businessUnitObjectiveRepository.findByBusinessUnitId(businessUnitIdObject);
        return businessUnitsObjectives.stream().map(m -> m.toDto()).collect(Collectors.toList());
    }


    @Override
    public BusinessUnitObjectiveDto patchObjective(long objectiveId, BusinessUnitObjectiveDto objectiveDto) throws ObjectiveNotFoundException, BusinessUnitNotFoundException, UserNotFoundException {
        BusinessUnitObjective objective = findObjective(objectiveId);

        patchObjective(objective, objectiveDto);
        objective.setRepresented(new ArrayList<CompanyKeyResult>());

        businessUnitObjectiveRepository.save(objective);

        objectiveDto.setId(objective.getId());

        businessUnitObjectiveRepository.save(objective);
        return objective.toDto();
    }






    





    
}
