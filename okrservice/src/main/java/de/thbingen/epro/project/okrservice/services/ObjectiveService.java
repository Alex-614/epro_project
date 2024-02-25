package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.ObjectiveRepository;

/**
 * 
 * Provides methods to interact with a Objective based on a ObjectiveRepository
 * 
 * @see Objective
 * @see ObjectiveRepository
 */
public interface ObjectiveService<T extends Objective> {


    /**
     * Find an Objective by its ID.
     * 
     * @param objectiveId unique identifier used to search for the Objective
     * @return returns the Objective 
     * @throws ObjectiveNotFoundException if the Objective was not found
     */
    T findObjective(long objectiveId) throws ObjectiveNotFoundException;


    /**
     * Deletes a Objective from a repository based on its ID.
     * 
     * If the Objective was not found it is silently ignored.
     * 
     * @param objectiveId unique identifier of the Objective that needs to be deleted.
     */
    void deleteObjective(long objectiveId);



}
