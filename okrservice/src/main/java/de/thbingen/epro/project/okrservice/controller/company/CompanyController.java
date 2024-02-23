package de.thbingen.epro.project.okrservice.controller.company;

import java.util.List;
import java.util.stream.Collectors;

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

import de.thbingen.epro.project.okrservice.constants.Roles;
import de.thbingen.epro.project.okrservice.controller.Utils;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleAssignmentRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private RoleAssignmentRepository roleAssignmentRepository;
    private Utils utils;

    @Autowired
    public CompanyController(CompanyRepository companyRepository, UserRepository userRepository, 
                            RoleRepository roleRepository, RoleAssignmentRepository roleAssignmentRepository, 
                            Utils utils) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.utils = utils;
    }



    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CompanyDto companyDto, 
                                                @AuthenticationPrincipal UserDetails userDetails) {
        Company company = new Company();
        company.setName(companyDto.getName());
        companyRepository.save(company);
        
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.addCompany(company);
        //userRepository.save(user)

        Role role = roleRepository.findByName(Roles.CO_OKR_ADMIN.getName());
        roleAssignmentRepository.save(new RoleAssignment(user, role, company));
        //TODO remove
        role = roleRepository.findByName(Roles.BUO_OKR_ADMIN.getName());
        roleAssignmentRepository.save(new RoleAssignment(user, role, company));


        companyDto.setId(company.getId());
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @GetMapping("{companyId}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Number companyId) throws Exception {
        Company company = utils.getCompanyFromRepository(companyId);
        return new ResponseEntity<>(new CompanyDto(company), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return new ResponseEntity<>(companies.stream()
                .map(CompanyDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PatchMapping("{companyId}")
    public ResponseEntity<CompanyDto> patchCompany(@PathVariable Number companyId,
                                                   @RequestBody CompanyDto companyDto) throws Exception {
        Company company = utils.getCompanyFromRepository(companyId);
        if (companyDto.getName() != null) company.setName(companyDto.getName());
        companyRepository.save(company);
        return new ResponseEntity<>(new CompanyDto(company), HttpStatus.OK);
    }


    @DeleteMapping("{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Number companyId) throws Exception {
        Company company = utils.getCompanyFromRepository(companyId);
        companyRepository.deleteById(companyId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    




    
    @PostMapping("{companyId}/user/{userId}/add")
    public ResponseEntity<String> addUser(@PathVariable @NonNull Number companyId, 
                                            @PathVariable @NonNull Number userId, 
                                            @RequestBody @Valid RolesDto roleDto) {
        if (!companyRepository.existsById(companyId.longValue())) {
            return new ResponseEntity<>("Company not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!userRepository.existsById(userId.longValue())) {
            return new ResponseEntity<>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        User target = userRepository.findById(userId.longValue()).get();

        for (Number roleId : roleDto.getRoleIds()) {
            if (roleRepository.existsById(roleId.longValue())) {
                Role role = roleRepository.findById(roleId.longValue()).get();
                roleAssignmentRepository.save(new RoleAssignment(target, role, company));
            }
        }

        return new ResponseEntity<>("User added to company", HttpStatus.OK);
    }


    @PostMapping("{companyId}/user/{userId}/remove")
    public ResponseEntity<String> removeUser(@PathVariable @NonNull Number companyId, 
                                                @PathVariable @NonNull Number userId) {
        if (!companyRepository.existsById(companyId.longValue())) {
            return new ResponseEntity<>("Company not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!userRepository.existsById(userId.longValue())) {
            return new ResponseEntity<>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        User target = userRepository.findById(userId.longValue()).get();

        roleAssignmentRepository.deleteByCompanyAndUserEquals(company, target);
        target.removeCompany(company);
        //userRepository.save(target);

        return new ResponseEntity<>("User added to company", HttpStatus.OK);
    }



}
