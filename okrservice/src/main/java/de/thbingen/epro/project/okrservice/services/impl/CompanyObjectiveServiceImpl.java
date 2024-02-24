package de.thbingen.epro.project.okrservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.MaxObjectivesReachedException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.services.CompanyObjectiveService;
import de.thbingen.epro.project.okrservice.services.CompanyService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class CompanyObjectiveServiceImpl extends ObjectiveServiceImpl<CompanyObjective, CompanyObjectiveDto> implements CompanyObjectiveService {

    private CompanyService companyService;

    private CompanyObjectiveRepository companyObjectiveRepository;

    @Autowired
    public CompanyObjectiveServiceImpl(UserService userService, CompanyService companyService, CompanyObjectiveRepository companyObjectiveRepository) {
        super(userService, companyObjectiveRepository);
        this.companyService = companyService;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }

    @Override
    public CompanyObjectiveDto createObjective(long companyId, CompanyObjectiveDto objectiveDto) throws CompanyNotFoundException, MaxObjectivesReachedException, UserNotFoundException {
        Company company = companyService.findCompany(companyId);

        // TODO just use SQL Count (new repository method) to not fetch all data 
        if (company.getObjectives().size() >= 5) {
            throw new MaxObjectivesReachedException();
        }

        CompanyObjective objective = new CompanyObjective();
        patchObjective(objective, objectiveDto);
        objective.setCompany(company);

        companyObjectiveRepository.save(objective);

        return objective.toDto();
    }


    @Override
    public List<CompanyObjectiveDto> findAllObjectives(long companyId) {
        return companyObjectiveRepository.findByCompanyId(companyId).stream().map(m -> m.toDto()).collect(Collectors.toList());
    }

    @Override
    public CompanyObjectiveDto patchObjective(long objectiveId, CompanyObjectiveDto objectiveDto) throws ObjectiveNotFoundException, UserNotFoundException {
        CompanyObjective objective = findObjective(objectiveId);
        patchObjective(objective, objectiveDto);
        companyObjectiveRepository.save(objective);
        return new CompanyObjectiveDto(objective);
    }











}
