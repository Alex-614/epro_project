package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;

public interface ObjectiveService<T extends Objective> {

    T findObjective(long objectiveId) throws ObjectiveNotFoundException;
    void deleteObjective(long objectiveId);



}
