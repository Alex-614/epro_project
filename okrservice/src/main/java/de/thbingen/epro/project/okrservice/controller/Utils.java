package de.thbingen.epro.project.okrservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.KeyResultNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.ObjectiveNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

@Component
public class Utils {


    private CompanyRepository companyRepository;
    private BusinessUnitRepository businessUnitRepository;
    private UserRepository userRepository;
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private CompanyObjectiveRepository companyObjectiveRepository;
    private UnitRepository unitRepository;
    private CompanyKeyResultRepository companyKeyResultRepository;
    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private KeyResultRepository keyResultRepository;

    @Autowired
    public Utils(CompanyRepository companyRepository, 
                    BusinessUnitRepository businessUnitRepository, 
                    UserRepository userRepository, 
                    BusinessUnitObjectiveRepository businessUnitObjectiveRepository, 
                    CompanyObjectiveRepository companyObjectiveRepository, 
                    UnitRepository unitRepository, 
                    CompanyKeyResultRepository companyKeyResultRepository, 
                    BusinessUnitKeyResultRepository businessUnitKeyResultRepository, 
                    KeyResultRepository keyResultRepository) {
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.userRepository = userRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.unitRepository = unitRepository;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.keyResultRepository = keyResultRepository;
    }




    public Company getCompanyFromRepository(Number companyId) throws Exception {
        Optional<Company> optionalCompany = companyRepository.findById(companyId.longValue());
        if(optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            throw new CompanyNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public BusinessUnit getBusinessUnitFromRepository(Number companyId, Number businessUnitId) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnit> optionalBusinessUnitItem = businessUnitRepository.findById(businessUnitIdObject);
        if(optionalBusinessUnitItem.isPresent()) {
            return optionalBusinessUnitItem.get();
        }
        else if(!businessUnitRepository.existsById(businessUnitIdObject)) {
            // "Business unit not found!"
            throw new BusinessUnitNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public User getUserFromRepository(Number userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId.longValue());
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else if(!userRepository.existsById(userId.longValue())) {
            throw new UserNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public BusinessUnitObjective getBusinessUnitObjectiveFromRepository (Number companyId, Number businessUnitId, Number businessUnitObjectiveId) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnitObjective> optionalBusinessUnitObjective =
                businessUnitObjectiveRepository.findById(businessUnitObjectiveId.longValue());
        if(optionalBusinessUnitObjective.isPresent()) {
            return optionalBusinessUnitObjective.get();
        }
        else if(!businessUnitRepository.existsById(businessUnitIdObject)) {
            // "Business unit not found!"
            throw new BusinessUnitNotFoundException();
        }
        else if (!businessUnitObjectiveRepository.existsById(businessUnitObjectiveId.longValue())) {
            throw new ObjectiveNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public CompanyObjective getCompanyObjectiveFromRepository (Number companyId, Number companyObjectiveId) throws Exception {
        Optional<CompanyObjective> optionalCompanyObjective =
                companyObjectiveRepository.findById(companyObjectiveId.longValue());
        if(optionalCompanyObjective.isPresent()) {
            return optionalCompanyObjective.get();
        }
        else if (!companyObjectiveRepository.existsById(companyObjectiveId.longValue())) {
            throw new ObjectiveNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public Unit getUnitFromRepository(Number companyId, Number businessUnitId, Number unitId
    ) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        UnitId unitIdObject = new UnitId(unitId.longValue(), businessUnitIdObject);
        Optional<Unit> optionalUnit = unitRepository.findById(unitIdObject);
        if(optionalUnit.isPresent()) {
            return optionalUnit.get();
        }
        else if (!businessUnitRepository.existsById(businessUnitIdObject)) {
            // "Business unit not found!"
            throw new BusinessUnitNotFoundException();
        }
        else if (!unitRepository.existsById(unitIdObject)) {
            // "Unit not found!"
            throw new UnitNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public KeyResult getKeyResultFromRepository(Number keyResultId) throws Exception {
        Optional<KeyResult> optionalKeyResult = keyResultRepository.findById(keyResultId.longValue());
        if(optionalKeyResult.isPresent()) {
            return optionalKeyResult.get();
        }
        throw new KeyResultNotFoundException();
    }

    public CompanyKeyResult getCompanyKeyResultFromRepository(Number companyId, Number companyObjectiveId, Number companyKeyResultId) throws Exception {
        Optional<CompanyKeyResult> optionalCompanyKeyResult =
                companyKeyResultRepository.findById(companyKeyResultId.longValue());
        if(optionalCompanyKeyResult.isPresent()) {
            return optionalCompanyKeyResult.get();
        }
        else if (!companyObjectiveRepository.existsById(companyObjectiveId.longValue())) {
            // "Company objective not found!"
            throw new ObjectiveNotFoundException();
        }
        else if (!companyKeyResultRepository.existsById(companyKeyResultId.longValue())) {
            // "Company key result not found!"
            throw new KeyResultNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public BusinessUnitKeyResult getBusinessUnitKeyResultFromRepository(Number companyId, Number businessUnitId, Number businessUnitObjectiveId, Number businessUnitKeyResultId
    ) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnitKeyResult> optionalBusinessUnitKeyResult =
                businessUnitKeyResultRepository.findById(businessUnitKeyResultId.longValue());
        if(optionalBusinessUnitKeyResult.isPresent()) {
            return optionalBusinessUnitKeyResult.get();
        }
        else if(!businessUnitRepository.existsById(businessUnitIdObject)) {
            // "Business unit not found!"
            throw new BusinessUnitNotFoundException();
        }
        else if (!businessUnitObjectiveRepository.existsById(businessUnitObjectiveId.longValue())) {
            throw new ObjectiveNotFoundException();
        }
        else if(!businessUnitKeyResultRepository.existsById(businessUnitKeyResultId.longValue())) {
            throw new KeyResultNotFoundException();
        }
        else {
            throw new Exception();
        }
    }
}
