package de.thbingen.epro.project.okrservice.controller.businessunit;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.services.BusinessUnitKeyResultService;
import de.thbingen.epro.project.okrservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
public class BusinessUnitKeyResultController {

    private BusinessUnitKeyResultService businessUnitKeyResultService;
    private UserService userService;

    @Autowired
    public BusinessUnitKeyResultController(BusinessUnitKeyResultService businessUnitKeyResultService, UserService userService) {
        this.businessUnitKeyResultService = businessUnitKeyResultService;
        this.userService = userService;
    }





    @PostMapping
    public ResponseEntity<KeyResultDto> createBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody @Valid BusinessUnitKeyResultDto keyResultDto) throws Exception {
        BusinessUnitKeyResultDto response = businessUnitKeyResultService.createKeyResult(companyId.longValue(), businessUnitId.longValue(), objectiveId.longValue(), keyResultDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    


    @GetMapping("{keyResultId}")
    public ResponseEntity<BusinessUnitKeyResultDto> getBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                                @PathVariable @NonNull Number businessUnitId,
                                                                                @PathVariable @NonNull Number objectiveId,
                                                                                @PathVariable @NonNull Number keyResultId)
            throws Exception {
        BusinessUnitKeyResult response = businessUnitKeyResultService.findKeyResult(keyResultId.longValue());
        return new ResponseEntity<>(response.toDto(), HttpStatus.OK);
    }



    @GetMapping("{keyResultId}/contributing/units")
    public ResponseEntity<List<UnitDto>> getContributingUnits(@PathVariable @NonNull Number companyId,
                                                                @PathVariable @NonNull Number businessUnitId,
                                                                @PathVariable @NonNull Number objectiveId,
                                                                @PathVariable @NonNull Number keyResultId)
            throws Exception {
        List<UnitDto> response = businessUnitKeyResultService.findContributingUnits(keyResultId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("{keyResultId}/contributing/businessunits")
    public ResponseEntity<List<BusinessUnitDto>> getContributingBusinessUnits(@PathVariable @NonNull Number companyId,
                                                                                @PathVariable @NonNull Number businessUnitId,
                                                                                @PathVariable @NonNull Number objectiveId,
                                                                                @PathVariable @NonNull Number keyResultId)
            throws Exception {
        List<BusinessUnitDto> response = businessUnitKeyResultService.findContributingBusinessUnits(keyResultId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @GetMapping
    public ResponseEntity<List<BusinessUnitKeyResultDto>> getAllBusinessUnitKeyResults(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId)
        throws Exception {
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = businessUnitKeyResultService.findAllKeyResults(objectiveId.longValue());
        return new ResponseEntity<>(businessUnitKeyResultDtos, HttpStatus.OK);
    }

    @GetMapping("{keyResultId}/updatehistory")
    public ResponseEntity<LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>>> getBusinessUnitKeyResultUpdateHistory(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number businessUnitId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>> response = businessUnitKeyResultService.findKeyResultUpdateHistory(keyResultId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{keyResultId}")
    public ResponseEntity<KeyResultDto> patchBusinessUnitKeyResult(@AuthenticationPrincipal UserDetails userDetails, 
                                                                    @PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number businessUnitId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @PathVariable @NonNull Number keyResultId,
                                                                    @RequestBody @Valid KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto)
            throws Exception {
        if (keyResultPatchDto.getUpdaterId() == null) {
            keyResultPatchDto.setUpdaterId(userService.findUserByEmail(userDetails.getUsername()).getId().longValue());
        }
        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResultService.patchKeyResult(keyResultId.longValue(), keyResultPatchDto);
        return new ResponseEntity<>(businessUnitKeyResultDto, HttpStatus.OK);
    }



    @DeleteMapping("{keyResultId}")
    public ResponseEntity<Void> deleteBusinessUnitKeyResult(@PathVariable @NonNull Number companyId,
                                                                             @PathVariable @NonNull Number businessUnitId,
                                                                             @PathVariable @NonNull Number objectiveId,
                                                                             @PathVariable @NonNull Number keyResultId)
            throws Exception {
        businessUnitKeyResultService.deleteKeyResult(keyResultId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    
}
