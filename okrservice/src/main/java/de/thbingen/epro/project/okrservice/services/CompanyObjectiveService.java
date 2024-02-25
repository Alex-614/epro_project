package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface CompanyObjectiveService extends ObjectiveService<CompanyObjective> {

    /**
     * 
     * Creates a new CompanyObjective and persists it. 
     * 
     * @param companyId unique identifier of the company that creates the objective 
     * @param objectiveDto contains the data to create the Company
     * @return the corresponding CompanyObjectiveDto
     * @throws MaxObjectivesReachedException if the Company already has 5 related Objectives
     * @throws CompanyNotFoundException if the Company was not found
     * @throws UserNotFoundException if the User was not found
     * 
     */
    CompanyObjectiveDto createObjective(long companyId, CompanyObjectiveDto objectiveDto) 
        throws MaxObjectivesReachedException, CompanyNotFoundException, UserNotFoundException;



    /**
     * 
     * Searches for all CompanyObjectives related to the Company. 
     * Never returns Null.
     * 
     * @param companyId used to search for the Company
     * @return a List of the CompanyObjectiveDtos 
     */
    List<CompanyObjectiveDto> findAllObjectives(long companyId);



    /**
     * 
     * Changes the CompanyObjective and persists. 
     * Only valid values will be changed.
     * 
     * @param objectiveId
     * @param objectiveDto
     * @return the corresponding CompanyObjectiveDto
     * @throws ObjectiveNotFoundException if the CompanyObjective was not found
     * @throws UserNotFoundException if the User was not found
     */
    CompanyObjectiveDto patchObjective(long objectiveId, CompanyObjectiveDto objectiveDto) 
        throws ObjectiveNotFoundException, UserNotFoundException;


}
