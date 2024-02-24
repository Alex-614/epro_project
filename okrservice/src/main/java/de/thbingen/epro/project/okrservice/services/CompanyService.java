package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface CompanyService {

    Company findCompany(long companyId) throws CompanyNotFoundException;
    CompanyDto createCompany(long userId, CompanyDto companyDto) throws UserNotFoundException;
    List<CompanyDto> getAllCompanies();
    CompanyDto patchCompany(long companyId, CompanyDto companyDto) throws CompanyNotFoundException;
    void deleteCompany(long companyId);
    void addUser(long companyId, long userId, RolesDto rolesDto) throws CompanyNotFoundException, UserNotFoundException;
    void removeUser(long companyId, long userId) throws CompanyNotFoundException, UserNotFoundException;


}
