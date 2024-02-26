package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface BusinessUnitObjectiveService extends ObjectiveService<BusinessUnitObjective> {

    /**
     * 
     * Creates a new BusinessUnitObjective and persists it. 
     * 
     * @param companyId unique identifier of the Company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param objectiveDto contains the data to create the BusinessUnit
     * @return the corresponding BusinessUnitObjectiveDto
     * @throws MaxObjectivesReachedException if the BusinessUnit already has 5 related Objectives
     * @throws BusinessUnitNotFoundException if the BusinessUnit was not found
     * @throws UserNotFoundException if the User was not found
     * @throws KeyResultNotFoundException 
     */
    BusinessUnitObjectiveDto createObjective(long companyId, long businessUnitId, BusinessUnitObjectiveDto objectiveDto) 
        throws MaxObjectivesReachedException, BusinessUnitNotFoundException, UserNotFoundException, KeyResultNotFoundException;



    /**
     * 
     * Searches for all BusinessUnitObjectives related to the BusinessUnit. 
     * Never returns Null.
     * 
     * @param companyId unique identifier of the Company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @return a List of the BusinessUnitObjectiveDto 
     */
    List<BusinessUnitObjectiveDto> findAllObjectives(long companyId, long businessUnitId);



    /**
     * 
     * Changes the BusinessUnitObjective and persists. 
     * Only valid values will be changed.
     * 
     * @param objectiveId
     * @param objectiveDto
     * @return the corresponding BusinessUnitObjective as DTO
     * @throws ObjectiveNotFoundException if the BusinessUnitObjective was not found
     * @throws UserNotFoundException if the User was not found
     * @throws KeyResultNotFoundException 
     */
    BusinessUnitObjectiveDto patchObjective(long objectiveId, BusinessUnitObjectiveDto objectiveDto) 
        throws ObjectiveNotFoundException, BusinessUnitNotFoundException, UserNotFoundException, KeyResultNotFoundException;











}
