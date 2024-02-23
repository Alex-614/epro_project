package de.thbingen.epro.project.okrservice.controller.company;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.controller.Utils;
import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective")
public class CompanyObjectiveController {



    private CompanyObjectiveRepository companyObjectiveRepository;

    private Utils utils;

    @Autowired
    public CompanyObjectiveController(CompanyObjectiveRepository companyObjectiveRepository, Utils utils) {
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.utils = utils;
    }




    @PostMapping
    public ResponseEntity<CompanyObjectiveDto> createCompanyObjective(@PathVariable @NonNull Number companyId,
                                                          @RequestBody @Valid CompanyObjectiveDto companyObjectiveDto)
            throws Exception {
        Company company = utils.getCompanyFromRepository(companyId);
        User owner = utils.getUserFromRepository(companyObjectiveDto.getOwnerId());

        if (company.getObjectives().size() >= 5) {
            // Reached Max Commpany Objectives
            return new ResponseEntity<>(companyObjectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CompanyObjective objective = new CompanyObjective();
        objective.setCompany(company);
        objective.setDeadline(companyObjectiveDto.getDeadline());
        objective.setDescription(companyObjectiveDto.getDescription());
        objective.setOwner(owner);
        objective.setTitle(companyObjectiveDto.getTitle());

        companyObjectiveRepository.save(objective);

        companyObjectiveDto.setId(objective.getId());
        return new ResponseEntity<>(companyObjectiveDto, HttpStatus.OK);
    }

    @GetMapping("{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> getCompanyObjective(@PathVariable Number companyId,
                                                                   @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective =
        utils.getCompanyObjectiveFromRepository(companyId, objectiveId);
        return new ResponseEntity<>(new CompanyObjectiveDto(companyObjective), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<CompanyObjectiveDto>> getAllCompanyObjectives(@PathVariable Number companyId) {
        List<CompanyObjective> companyObjectives = companyObjectiveRepository.findByCompanyId(companyId.longValue());
        return new ResponseEntity<>(companyObjectives.stream()
                .map(CompanyObjectiveDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    
    @PatchMapping("{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> patchCompanyObjective(
            @PathVariable Number companyId, @PathVariable Number objectiveId,
            @RequestBody CompanyObjectiveDto objectiveDto) throws Exception {
        CompanyObjective objective = utils.getCompanyObjectiveFromRepository(companyId, objectiveId.longValue());
        User owner = null;
        if (objectiveDto.getOwnerId() != null) owner = utils.getUserFromRepository(objectiveDto.getOwnerId());
        if (objectiveDto.getDeadline() != null) objective.setDeadline(objectiveDto.getDeadline());
        if (objectiveDto.getTitle() != null) objective.setTitle(objectiveDto.getTitle());
        if (objectiveDto.getDescription() != null) objective.setDescription(objectiveDto.getDescription());
        if (owner != null) objective.setOwner(owner);

        companyObjectiveRepository.save(objective);
        return new ResponseEntity<>(new CompanyObjectiveDto(objective), HttpStatus.OK);
    }



    @DeleteMapping("{objectiveId}")
    public ResponseEntity<Void> deleteCompanyObjective(@PathVariable Number companyId,
                                                         @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective = utils.getCompanyObjectiveFromRepository(companyId, objectiveId);
        companyObjectiveRepository.deleteById(objectiveId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
















}
