package de.thbingen.epro.project.okrservice.services.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.ObjectiveRepository;
import de.thbingen.epro.project.okrservice.services.ObjectiveService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public abstract class ObjectiveServiceImpl<T extends Objective, K extends ObjectiveDto> implements ObjectiveService<T> {

    private UserService userService;

    private ObjectiveRepository<T> objectiveRepository;

    @Autowired
    public ObjectiveServiceImpl(UserService userService, ObjectiveRepository<T> objectiveRepository) {
        this.userService = userService;
        this.objectiveRepository = objectiveRepository;
    }

    @Override
    public T findObjective(long objectiveId) throws ObjectiveNotFoundException {
        return objectiveRepository.findById(objectiveId).orElseThrow(() -> new ObjectiveNotFoundException());
    }

    protected void patchObjective(T objective, K objectiveDto) throws UserNotFoundException {
        User owner = null;
        if (objectiveDto.getOwnerId() != null) 
            owner = userService.findUser(objectiveDto.getOwnerId());
        if (owner != null) 
            objective.setOwner(owner);

        if (objectiveDto.getDeadline() != null && objectiveDto.getDeadline().longValue() >= 0) 
            objective.setDeadline(Instant.ofEpochMilli(objectiveDto.getDeadline()));
        
        if (objectiveDto.getTitle() != null && !objectiveDto.getTitle().trim().isEmpty()) 
            objective.setTitle(objectiveDto.getTitle());
        
        if (objectiveDto.getDescription() != null && !objectiveDto.getDescription().trim().isEmpty()) 
            objective.setDescription(objectiveDto.getDescription());
    }


    @Override
    public void deleteObjective(long objectiveId) {
        objectiveRepository.deleteById(objectiveId);
    }















}
