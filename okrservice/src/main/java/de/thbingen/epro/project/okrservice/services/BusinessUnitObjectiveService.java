package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface BusinessUnitObjectiveService extends ObjectiveService<BusinessUnitObjective> {


    BusinessUnitObjectiveDto createObjective(long companyId, long businessUnitId, BusinessUnitObjectiveDto objectiveDto) throws MaxObjectivesReachedException, BusinessUnitNotFoundException, UserNotFoundException;
    List<BusinessUnitObjectiveDto> findAllObjectives(long companyId, long businessUnitId);
    BusinessUnitObjectiveDto patchObjective(long companyId, long businessUnitId, long objectiveId, BusinessUnitObjectiveDto objectiveDto) throws ObjectiveNotFoundException, BusinessUnitNotFoundException, UserNotFoundException;











}
