package de.thbingen.epro.project.okrservice.controller.businessunit;

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

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.services.BusinessUnitObjectiveService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective")
public class BusinessUnitObjectiveController {

    private BusinessUnitObjectiveService businessUnitObjectiveService;


    @Autowired
    public BusinessUnitObjectiveController(BusinessUnitObjectiveService businessUnitObjectiveService) {
        this.businessUnitObjectiveService = businessUnitObjectiveService;
    }






    @PostMapping
    public ResponseEntity<BusinessUnitObjectiveDto> createBusinessUnitObjective(@PathVariable @NonNull Number companyId, 
                                                                        @PathVariable @NonNull Number businessUnitId, 
                                                                        @RequestBody @Valid BusinessUnitObjectiveDto objectiveDto
    ) throws Exception {
        BusinessUnitObjectiveDto response = businessUnitObjectiveService.createObjective(companyId.longValue(), businessUnitId.longValue(), objectiveDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(@PathVariable @NonNull Number companyId,
                                                                        @PathVariable @NonNull Number businessUnitId){
        List<BusinessUnitObjectiveDto> response = businessUnitObjectiveService.findAllObjectives(companyId.longValue(), businessUnitId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{objectiveId}")
    public ResponseEntity<BusinessUnitObjectiveDto> getBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId
    ) throws Exception {
        BusinessUnitObjective response = businessUnitObjectiveService.findObjective(objectiveId.longValue());
        return new ResponseEntity<>(response.toDto(), HttpStatus.OK);
    }

    @PatchMapping("{objectiveId}")
    public ResponseEntity<BusinessUnitObjectiveDto> patchBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody BusinessUnitObjectiveDto objectiveDto
    ) throws Exception {
        BusinessUnitObjectiveDto response = businessUnitObjectiveService.patchObjective(companyId.longValue(), businessUnitId.longValue(), objectiveId.longValue(), objectiveDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("{objectiveId}")
    public ResponseEntity<Void> deleteBusinessUnitObjective(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @PathVariable @NonNull Number objectiveId
    ) throws Exception {
        businessUnitObjectiveService.deleteObjective(objectiveId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }















}
