package de.thbingen.epro.project.okrservice.controller.company;

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

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.services.CompanyService;
import de.thbingen.epro.project.okrservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;
    private UserService userService;

    @Autowired
    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CompanyDto companyDto, 
                                                @AuthenticationPrincipal UserDetails userDetails) throws UserAlreadyExistsException, UserNotFoundException {
        CompanyDto response = companyService.createCompany(userService.findUserByEmail(userDetails.getUsername()).getId().longValue(), companyDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{companyId}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Number companyId) throws Exception {
        Company response = companyService.findCompany(companyId.longValue());
        return new ResponseEntity<>(new CompanyDto(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> response = companyService.findAllCompanies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{companyId}")
    public ResponseEntity<CompanyDto> patchCompany(@PathVariable Number companyId,
                                                   @RequestBody CompanyDto companyDto) throws Exception {
        CompanyDto response = companyService.patchCompany(companyId.longValue(), companyDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Number companyId) throws Exception {
        companyService.deleteCompany(companyId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    




    
    @PostMapping("{companyId}/user/{userId}/add")
    public ResponseEntity<String> addUser(@PathVariable @NonNull Number companyId, 
                                            @PathVariable @NonNull Number userId, 
                                            @RequestBody @Valid RolesDto roleDto) throws CompanyNotFoundException, UserNotFoundException {
        companyService.addUser(companyId.longValue(), userId.longValue(), roleDto);
        return new ResponseEntity<>("User added to company", HttpStatus.OK);
    }


    @PostMapping("{companyId}/user/{userId}/remove")
    public ResponseEntity<String> removeUser(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number userId) throws CompanyNotFoundException, UserNotFoundException {
        companyService.removeUser(companyId.longValue(), userId.longValue());
        return new ResponseEntity<>("User added to company", HttpStatus.OK);
    }



}
