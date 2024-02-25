package de.thbingen.epro.project.okrservice.controller.company;

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
import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultUpdateDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.services.CompanyKeyResultService;
import de.thbingen.epro.project.okrservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective/{objectiveId}/keyresult")
public class CompanyKeyResultController {



    private CompanyKeyResultService companyKeyResultService;
    private UserService userService;



    @Autowired
    public CompanyKeyResultController(CompanyKeyResultService companyKeyResultService, UserService userService) {
        this.companyKeyResultService = companyKeyResultService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CompanyKeyResultDto> createCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number objectiveId,
                                                                    @RequestBody @Valid CompanyKeyResultDto keyResultDto) throws Exception {
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.createKeyResult(objectiveId.longValue(), keyResultDto);
        return new ResponseEntity<>(companyKeyResultDto, HttpStatus.OK);
    }

    @GetMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> getCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult = companyKeyResultService.findKeyResult(keyResultId.longValue());
        return new ResponseEntity<>(companyKeyResult.toDto(), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<CompanyKeyResultDto>> getAllBCompanyKeyResults(@PathVariable @NonNull Number companyId,
                                                                    @PathVariable @NonNull Number objectiveId)
        throws Exception {
        List<CompanyKeyResultDto> companyKeyResultDtos = companyKeyResultService.findAllKeyResults(objectiveId.longValue());
        return new ResponseEntity<>(companyKeyResultDtos, HttpStatus.OK);
    }

    @GetMapping("{keyResultId}/contributing/units")
    public ResponseEntity<List<UnitDto>> getContibutingUnits(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        return new ResponseEntity<>(companyKeyResultService.findContributingUnits(keyResultId.longValue()), HttpStatus.OK);
    }
    @GetMapping("{keyResultId}/contributing/businessunits")
    public ResponseEntity<List<BusinessUnitDto>> getContibutingBusinessUnits(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        return new ResponseEntity<>(companyKeyResultService.findContributingBusinessUnits(keyResultId.longValue()), HttpStatus.OK);
    }


    @GetMapping("{keyResultId}/updatehistory")
    public ResponseEntity<LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>>> getCompanyKeyResultUpdateHistory(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>> response = companyKeyResultService.findKeyResultUpdateHistory(keyResultId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PatchMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> patchCompanyKeyResult(@AuthenticationPrincipal UserDetails userDetails, 
                                                    @PathVariable @NonNull Number companyId,
                                                    @PathVariable @NonNull Number objectiveId,
                                                    @PathVariable @NonNull Number keyResultId,
                                                    @RequestBody @Valid KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto)
                                                throws Exception {
        if (keyResultPatchDto.getUpdaterId() == null) {
            keyResultPatchDto.setUpdaterId(userService.findUserByEmail(userDetails.getUsername()).getId().longValue());
        }
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.patchKeyResult(keyResultId.longValue(), keyResultPatchDto);
        return new ResponseEntity<>(companyKeyResultDto, HttpStatus.OK);
    }

    @DeleteMapping("{keyResultId}")
    public ResponseEntity<Void> deleteCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                            @PathVariable @NonNull Number objectiveId,
                                                            @PathVariable @NonNull Number keyResultId) throws Exception {
        companyKeyResultService.deleteKeyResult(keyResultId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }













}
