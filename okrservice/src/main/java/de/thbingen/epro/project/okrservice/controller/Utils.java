package de.thbingen.epro.project.okrservice.controller;

import java.util.Optional;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
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
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

public class Utils {
    public static Company getCompanyFromRepository(CompanyRepository companyRepository, Number companyId) throws Exception {
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

    public static BusinessUnit getBusinessUnitFromRepository(CompanyRepository companyRepository,
                                                             Number companyId,
                                                             BusinessUnitRepository businessUnitRepository,
                                                             Number businessUnitId) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnit> optionalBusinessUnitItem = businessUnitRepository.findById(businessUnitIdObject);
        if(optionalBusinessUnitItem.isPresent()) {
            return optionalBusinessUnitItem.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
        }
        else if(!businessUnitRepository.existsById(businessUnitIdObject)) {
            // "Business unit not found!"
            throw new BusinessUnitNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public static User getUserFromRepository(UserRepository userRepository, Number userId) throws Exception {
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

    public static BusinessUnitObjective getBusinessUnitObjectiveFromRepository (
            CompanyRepository companyRepository, Number companyId, BusinessUnitRepository businessUnitRepository,
            Number businessUnitId, BusinessUnitObjectiveRepository businessUnitObjectiveRepository,
            Number businessUnitObjectiveId) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnitObjective> optionalBusinessUnitObjective =
                businessUnitObjectiveRepository.findById(businessUnitObjectiveId.longValue());
        if(optionalBusinessUnitObjective.isPresent()) {
            return optionalBusinessUnitObjective.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
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

    public static CompanyObjective getCompanyObjectiveFromRepository (
            CompanyRepository companyRepository, Number companyId,
            CompanyObjectiveRepository companyObjectiveRepository,
            Number companyObjectiveId) throws Exception {
        Optional<CompanyObjective> optionalCompanyObjective =
                companyObjectiveRepository.findById(companyObjectiveId.longValue());
        if(optionalCompanyObjective.isPresent()) {
            return optionalCompanyObjective.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
        }
        else if (!companyObjectiveRepository.existsById(companyObjectiveId.longValue())) {
            throw new ObjectiveNotFoundException();
        }
        else {
            throw new Exception();
        }
    }

    public static Unit getUnitFromRepository(
            CompanyRepository companyRepository, Number companyId, BusinessUnitRepository businessUnitRepository,
            Number businessUnitId, UnitRepository unitRepository, Number unitId
    ) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        UnitId unitIdObject = new UnitId(unitId.longValue(), businessUnitIdObject);
        Optional<Unit> optionalUnit = unitRepository.findById(unitIdObject);
        if(optionalUnit.isPresent()) {
            return optionalUnit.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
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

    public static CompanyKeyResult getCompanyKeyResultFromRepository(
            CompanyRepository companyRepository, Number companyId,
            CompanyObjectiveRepository companyObjectiveRepository, Number companyObjectiveId,
            CompanyKeyResultRepository companyKeyResultRepository, Number companyKeyResultId) throws Exception {
        Optional<CompanyKeyResult> optionalCompanyKeyResult =
                companyKeyResultRepository.findById(companyKeyResultId.longValue());
        if(optionalCompanyKeyResult.isPresent()) {
            return optionalCompanyKeyResult.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
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

    public static BusinessUnitKeyResult getBusinessUnitKeyResultFromRepository(
            CompanyRepository companyRepository, Number companyId,
            BusinessUnitRepository businessUnitRepository, Number businessUnitId,
            BusinessUnitObjectiveRepository businessUnitObjectiveRepository, Number businessUnitObjectiveId,
            BusinessUnitKeyResultRepository businessUnitKeyResultRepository, Number businessUnitKeyResultId
    ) throws Exception {
        BusinessUnitId businessUnitIdObject = new BusinessUnitId(businessUnitId.longValue(), companyId.longValue());
        Optional<BusinessUnitKeyResult> optionalBusinessUnitKeyResult =
                businessUnitKeyResultRepository.findById(businessUnitKeyResultId.longValue());
        if(optionalBusinessUnitKeyResult.isPresent()) {
            return optionalBusinessUnitKeyResult.get();
        }
        else if (!companyRepository.existsById(companyId.longValue())) {
            // "Company not found!"
            throw new CompanyNotFoundException();
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
