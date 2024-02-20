package de.thbingen.epro.project.okrservice.controller;

import de.thbingen.epro.project.okrservice.dtos.*;
import de.thbingen.epro.project.okrservice.entities.*;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import de.thbingen.epro.project.okrservice.Roles;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleAssignmentRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyController {

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private RoleAssignmentRepository roleAssignmentRepository;

    private CompanyObjectiveRepository companyObjectiveRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository, UserRepository userRepository, 
                            RoleRepository roleRepository, RoleAssignmentRepository roleAssignmentRepository, 
                            CompanyObjectiveRepository companyObjectiveRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }



    @PostMapping("/company")
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

        companyDto.setId(company.getId());
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @PatchMapping("/company/{companyId}")
    public ResponseEntity<CompanyDto> patchCompany(@PathVariable Number companyId,
                                                   @RequestBody CompanyDto companyDto) throws Exception {
        Company oldCompany = Utils.getCompanyFromRepository(companyRepository, companyId);
        CompanyDto oldCompanyDto = new CompanyDto(oldCompany);
        Field[] fields = CompanyDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(companyDto);
            if(value != null) {
                field.set(oldCompanyDto, value);
            }
            field.setAccessible(false);
        }
        oldCompany.setName(oldCompanyDto.getName());
        companyRepository.save(oldCompany);
        return new ResponseEntity<>(oldCompanyDto, HttpStatus.OK);
    }

    @GetMapping("/company")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return new ResponseEntity<>(companies.stream()
                .map(CompanyDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<CompanyDto> createCompany(@PathVariable Number companyId) throws Exception {
        Company company = Utils.getCompanyFromRepository(companyRepository, companyId);
        return new ResponseEntity<>(new CompanyDto(company), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable Number companyId) throws Exception {
        Company company = Utils.getCompanyFromRepository(companyRepository, companyId);
        companyRepository.deleteById(companyId.longValue());
        return new ResponseEntity<>(company.getName() + " deleted", HttpStatus.OK);
    }
    
    
    @PostMapping("/company/{companyId}/user/{userId}/add")
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


    @PostMapping("/company/{companyId}/user/{userId}/remove")
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



    
    @SuppressWarnings("null") // objectiveDto is validated, cant be null
    @PostMapping("/company/{companyId}/objective")
    public ResponseEntity<CompanyObjectiveDto> createCompanyObjective(@PathVariable @NonNull Number companyId,
                                                          @RequestBody @Valid CompanyObjectiveDto companyObjectiveDto)
            throws Exception {
        Company company = Utils.getCompanyFromRepository(companyRepository, companyId);
        User owner = Utils.getUserFromRepository(userRepository, companyObjectiveDto.getOwnerId());

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
    
    @PatchMapping("/company/{companyId}/objective/{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> patchCompanyObjective(
            @PathVariable Number companyId, @PathVariable Number objectiveId,
            @RequestBody CompanyObjectiveDto objectiveDto) throws Exception {
        CompanyObjective oldObjective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId,
                        companyObjectiveRepository, objectiveId.longValue());
        CompanyObjectiveDto oldObjectiveDto = new CompanyObjectiveDto(oldObjective);
        User owner = Utils.getUserFromRepository(userRepository, objectiveDto.getOwnerId());


        Field[] fields = ObjectiveDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(objectiveDto);
            if(value != null) {
                field.set(oldObjectiveDto, value);
            }
            field.setAccessible(false);
        }

        oldObjective.setDeadline(oldObjectiveDto.getDeadline());
        oldObjective.setTitle(oldObjectiveDto.getTitle());
        oldObjective.setDescription(oldObjectiveDto.getDescription());
        oldObjective.setOwner(owner);
        companyObjectiveRepository.save(oldObjective);
        return new ResponseEntity<>(oldObjectiveDto, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/objective")
    public ResponseEntity<List<CompanyObjectiveDto>> getAllCompanyObjectives(@PathVariable Number companyId) {
        List<CompanyObjective> companyObjectives = companyObjectiveRepository.findByCompanyId(companyId.longValue());
        return new ResponseEntity<>(companyObjectives.stream()
                .map(CompanyObjectiveDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}/objective/{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> getCompanyObjective(@PathVariable Number companyId,
                                                                   @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId);
        return new ResponseEntity<>(new CompanyObjectiveDto(companyObjective), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}/objective/{objectiveId}")
    public ResponseEntity<String> deleteCompanyObjective(@PathVariable Number companyId,
                                                         @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId);
        companyObjectiveRepository.deleteById(objectiveId.longValue());
        return new ResponseEntity<>(companyObjective.getTitle() + " deleted", HttpStatus.OK);
    }
}
