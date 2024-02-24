package de.thbingen.epro.project.okrservice.controller.company;

import java.util.List;

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

import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.services.CompanyObjectiveService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective")
public class CompanyObjectiveController {



    private CompanyObjectiveService companyObjectiveService;


    @Autowired
    public CompanyObjectiveController(CompanyObjectiveService companyObjectiveService) {
        this.companyObjectiveService = companyObjectiveService;
    }




    @PostMapping
    public ResponseEntity<CompanyObjectiveDto> createCompanyObjective(@PathVariable @NonNull Number companyId,
                                                          @RequestBody @Valid CompanyObjectiveDto objectiveDto)
            throws Exception {
        CompanyObjectiveDto response = companyObjectiveService.createObjective(companyId.longValue(), objectiveDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> getCompanyObjective(@PathVariable Number companyId,
                                                                   @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective = companyObjectiveService.findObjective(objectiveId.longValue());
        return new ResponseEntity<>(companyObjective.toDto(), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<CompanyObjectiveDto>> getAllCompanyObjectives(@PathVariable Number companyId) {
        List<CompanyObjectiveDto> response = companyObjectiveService.findAllObjectives(companyId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    
    @PatchMapping("{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> patchCompanyObjective(
            @PathVariable Number companyId, @PathVariable Number objectiveId,
            @RequestBody CompanyObjectiveDto objectiveDto) throws Exception {
        CompanyObjectiveDto response = companyObjectiveService.patchObjective(companyId.longValue(), objectiveDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @DeleteMapping("{objectiveId}")
    public ResponseEntity<Void> deleteCompanyObjective(@PathVariable Number companyId,
                                                         @PathVariable Number objectiveId) throws Exception {
        companyObjectiveService.deleteObjective(objectiveId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
















}
