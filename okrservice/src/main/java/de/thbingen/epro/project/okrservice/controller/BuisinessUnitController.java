package de.thbingen.epro.project.okrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.BuisinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.buisinessunit.BuisinessUnit;
import de.thbingen.epro.project.okrservice.entities.buisinessunit.BuisinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.company.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import de.thbingen.epro.project.okrservice.repositories.BuisinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BuisinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

@RestController
public class BuisinessUnitController {

    private CompanyRepository companyRepository;

    private BuisinessUnitRepository buisinessUnitRepository;
    private BuisinessUnitObjectiveRepository buisinessUnitObjectiveRepository;

    private UserRepository userRepository;


    @Autowired
    public BuisinessUnitController(CompanyRepository companyRepository, BuisinessUnitRepository buisinessUnitRepository, 
                                    BuisinessUnitObjectiveRepository buisinessUnitObjectiveRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.buisinessUnitRepository = buisinessUnitRepository;
        this.buisinessUnitObjectiveRepository = buisinessUnitObjectiveRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/company/{companyId}/buisinessunit")
    public ResponseEntity<BuisinessUnitDto> createBuisinessUnit(@PathVariable @NonNull Number companyId, @RequestBody BuisinessUnitDto buisinessUnitDto) {
        if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            buisinessUnitDto.setName("Company not found!");
            return new ResponseEntity<>(buisinessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Company company = companyRepository.findById(companyId.longValue()).get();
        if (buisinessUnitRepository.existsByNameAndCompanyIdEquals(buisinessUnitDto.getName(), company.getId())) {
            // "BuisinessUnit already exists!"
            buisinessUnitDto.setName("BuisinessUnit already exists!");
            return new ResponseEntity<>(buisinessUnitDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        BuisinessUnit buisinessUnit = new BuisinessUnit();
        buisinessUnit.setName(buisinessUnitDto.getName());
        buisinessUnit.setCompany(company);
        
        buisinessUnitRepository.save(buisinessUnit);

        buisinessUnitDto.setId(buisinessUnit.getId());
        return new ResponseEntity<>(buisinessUnitDto, HttpStatus.OK);
    }
    
    

    @SuppressWarnings("null") // objectiveDto is validated, cant be null
    @PostMapping("/company/{companyId}/buisinessunit/{buisinessUnitId}/objective")
    public ResponseEntity<ObjectiveDto> createBuisinessUnitObjective(@PathVariable @NonNull Number companyId, @PathVariable @NonNull Number buisinessUnitId, @RequestBody ObjectiveDto objectiveDto) {
        BuisinessUnitId buId = new BuisinessUnitId(buisinessUnitId.longValue(), companyId.longValue());
        if (!companyRepository.existsById(companyId.longValue())) {
            // Company not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!buisinessUnitRepository.existsById(buId)) {
            // BuisinessUnit not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!userRepository.existsById(objectiveDto.getOwnerId())) {
            // User not found!
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        BuisinessUnit buisinessUnit = buisinessUnitRepository.findById(buId).get();
        User owner = userRepository.findById(objectiveDto.getOwnerId()).get();

        if (buisinessUnit.getObjectives().size() >= 5) {
            // Reached Max Commpany Objectives
            return new ResponseEntity<>(objectiveDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BuisinessUnitObjective objective = new BuisinessUnitObjective();
        objective.setBuisinessunit(buisinessUnit);
        objective.setDeadline(objectiveDto.getDeadline());
        objective.setDescription(objectiveDto.getDescription());
        objective.setOwner(owner);
        objective.setTitle(objectiveDto.getTitle());

        buisinessUnitObjectiveRepository.save(objective);
        
        objectiveDto.setId(objective.getId());
        return new ResponseEntity<>(objectiveDto, HttpStatus.OK);
    }






}
