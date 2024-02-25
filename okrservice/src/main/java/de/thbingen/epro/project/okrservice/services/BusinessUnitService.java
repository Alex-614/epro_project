package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;

public interface BusinessUnitService {

    /**
     * 
     * Creates a new BusinessUnit related to a Company.
     * 
     * @param companyId unique identifier of the company that creates the BusinessUnit.
     * @param businessUnitDto contains the data to create the BusinessUnit from
     * @return the corresponing BusinessUnitDto
     * @throws CompanyNotFoundException
     * @throws BusinessUnitAlreadyExistsException
     */
    BusinessUnitDto createBusinessUnit(long companyId, BusinessUnitDto businessUnitDto) throws CompanyNotFoundException, BusinessUnitAlreadyExistsException;



    /**
     * 
     * Searches for all BusinessUnits related to a Company
     * 
     * @param companyId unique identifier of the related Company
     * @return a List of the found BusinessUnits as DTO
     */
    List<BusinessUnitDto> findAllBusinessUnits(long companyId);



    /**
     * Find a BusinessUnit by its ID.
     * 
     * @param companyId unique identifier used to search for the company
     * @param businessUnitId weak unique identifier of company to businessUnit
     * @return the BusinessUnit 
     * @throws BusinessUnitNotFoundException if the BusinessUnit was not found
     * 
     * @see BusinessUnitId
     */
    BusinessUnit findBusinessUnit(long companyId, long businessUnitId) throws BusinessUnitNotFoundException;


    /**
     * 
     * Changes a BusinessUnit and persists. 
     * 
     * @param companyId unique identifier of the Company that the BusinessUnit is from
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param businessUnitDto contains the changed data
     * @return the corresponding BusinessUnitDto
     * @throws BusinessUnitNotFoundException
     */
    BusinessUnitDto patchBusinessUnit(long companyId, long businessUnitId, BusinessUnitDto businessUnitDto) throws BusinessUnitNotFoundException;


    /**
     * Deletes a BusinessUnit from a repository based on its ID.
     * 
     * If the BusinessUnit was not found it is silently ignored.
     * 
     * @param companyId unique identifier used to search for the company
     * @param businessUnitId weak unique identifier of company to businessUnit
     * 
     * @see BusinessUnitId
     */
    void deleteBusinessUnit(long companyId, long businessUnitId);


}
