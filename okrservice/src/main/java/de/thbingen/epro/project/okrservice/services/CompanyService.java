package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface CompanyService {

    /**
     * Find a Company by its ID.
     * 
     * @param companyId unique identifier used to search for the company
     * @return returns the Company 
     * @throws CompanyNotFoundException if the Company was not found
     */
    Company findCompany(long companyId) throws CompanyNotFoundException;



    /**
     * 
     * Creates a new Company.
     * 
     * @param userId the User that creates the Company
     * @param companyDto contains the data to crete the Company from
     * @return the corresponding CompanyDto
     * @throws UserNotFoundException
     */
    CompanyDto createCompany(long userId, CompanyDto companyDto) throws UserNotFoundException;



    /**
     * 
     * Searches for all Companies in the repository.
     * 
     * @return a List of all Companies as Dto
     */
    List<CompanyDto> findAllCompanies();



    /**
     * 
     * Changes a Company. 
     * Only valid values are changed.
     * 
     * @param companyId unique identifier od the Company that needs to be changed 
     * @param companyDto contains the changed data
     * @return the corresponding CompanyDto
     * @throws CompanyNotFoundException
     */
    CompanyDto patchCompany(long companyId, CompanyDto companyDto) throws CompanyNotFoundException;



    /**
     * Deletes a Company from a repository based on its ID.
     * 
     * If the Company was not found it is silently ignored.
     * 
     * @param companyId unique identifier of the Company that needs to be deleted.
     */
    void deleteCompany(long companyId);



    /**
     * 
     * Adds a User to a Company with a set of Roles. 
     * The roles will be assigned only to the targetted User at that Company.
     * 
     * @param companyId unique ideftidier of the Company that the User needs to be added to
     * @param userId unique identifier of the targetted User
     * @param rolesDto contains all Roles
     * @throws CompanyNotFoundException
     * @throws UserNotFoundException
     */
    void addUser(long companyId, long userId, RolesDto rolesDto) throws CompanyNotFoundException, UserNotFoundException;
    
    
    
    /**
     * 
     * Removes a User from a Company. Also removes all RoleAssignments of that User at that. 
     * 
     * @param companyId unique ideftidier of the Company that the User needs to be added to
     * @param userId unique identifier of the targetted User
     * @throws CompanyNotFoundException
     * @throws UserNotFoundException
     */
    void removeUser(long companyId, long userId) throws CompanyNotFoundException, UserNotFoundException;


}
