package de.thbingen.epro.project.okrservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

@RestController
public class CompanyController {

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @PostMapping("/company")
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto, 
                                                @AuthenticationPrincipal UserDetails userDetails) {
        
        Company company = new Company();
        company.setName(companyDto.getName());
        companyRepository.save(company);
        
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.addCompany(company);

        companyDto.setId(company.getId());
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }
    
    
    @PostMapping("/company/{companyId}/user/{userId}/add")
    public ResponseEntity<String> addUser(@PathVariable @NonNull Number companyId, @PathVariable @NonNull Number userId, 
                                            @RequestBody List<Number> roles) {
        if (!userRepository.existsById(userId.longValue())) {
            return new ResponseEntity<>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!companyRepository.existsById(companyId.longValue())) {
            return new ResponseEntity<>("Company not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();

        Map<Company, Role> roleAssignments = new HashMap<Company, Role>();
        for (Number roleID : roles) {
            if (roleRepository.existsById(roleID.longValue())) {
                roleAssignments.put(company, roleRepository.findById(roleID.longValue()).get());
            }
        }

        User target = userRepository.findById(userId.longValue()).get();
        target.setRoleAssignments(roleAssignments);

        return new ResponseEntity<>("User added to company", HttpStatus.OK);
    }
    



}
