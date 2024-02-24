package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface CompanyObjectiveService extends ObjectiveService<CompanyObjective> {

    CompanyObjectiveDto createObjective(long companyId, CompanyObjectiveDto objectiveDto) throws MaxObjectivesReachedException, CompanyNotFoundException, UserNotFoundException;
    List<CompanyObjectiveDto> findAllObjectives(long companyId);
    CompanyObjectiveDto patchObjective(long objectiveId, CompanyObjectiveDto objectiveDto) throws ObjectiveNotFoundException, UserNotFoundException;


}
