package de.thbingen.epro.project.okrservice.controller.company;

import java.lang.reflect.Field;
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
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective")
public class CompanyObjectiveController {




    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private CompanyObjectiveRepository companyObjectiveRepository;

    @Autowired
    public CompanyObjectiveController(CompanyRepository companyRepository, UserRepository userRepository,  
                            CompanyObjectiveRepository companyObjectiveRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }




    @PostMapping
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

    @GetMapping("{objectiveId}")
    public ResponseEntity<CompanyObjectiveDto> getCompanyObjective(@PathVariable Number companyId,
                                                                   @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId);
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



    @DeleteMapping("{objectiveId}")
    public ResponseEntity<String> deleteCompanyObjective(@PathVariable Number companyId,
                                                         @PathVariable Number objectiveId) throws Exception {
        CompanyObjective companyObjective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId);
        companyObjectiveRepository.deleteById(objectiveId.longValue());
        return new ResponseEntity<>(companyObjective.getTitle() + " deleted", HttpStatus.OK);
    }
















}
