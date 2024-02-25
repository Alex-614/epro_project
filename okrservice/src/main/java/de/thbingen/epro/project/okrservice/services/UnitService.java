package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UnitNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;

/**
 * 
 * provides methods to interact with a Unit based on a UnitRepository
 * 
 * @see Unit
 * @see UnitRepository
 */
public interface UnitService {



    /**
     * 
     * Creates a new Unit and persisits it.
     * 
     * @param companyId unique identifier of company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param unitDto contains data to create the Unit
     * @return the corresponding UnitDto 
     * @throws UnitAlreadyExistsException if a Unitwith the same name already exists
     * @throws BusinessUnitNotFoundException if the BusinessUnit was not found
     * 
     * @see BusinessUnitService#findBusinessUnit(long, long)
     */
    UnitDto createUnit(long companyId, long businessUnitId, UnitDto unitDto) 
        throws UnitAlreadyExistsException, BusinessUnitNotFoundException;



    /**
     * Finds all Units related to a BusinessUnit.
     * 
     * @param companyId unique identifier of company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @return returns a List of 
     * @throws UnitNotFoundException if the Unit was not found
     * 
     * @see BusinessUnitService#findBusinessUnit(long, long)
     */
    List<UnitDto> findAllUnits(long companyId, long businessUnitId);



    /**
     * Find a Unit by its ID.
     * 
     * @param companyId unique identifier of company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param unitId partially unique identifier of the Unit
     * @return returns the Unit 
     * @throws UnitNotFoundException if the Unit was not found
     * 
     * @see UnitId
     */
    Unit findUnit(long companyId, long businessUnitId, long unitId) throws UnitNotFoundException;


    
    /**
     * 
     * Changes a Unit.
     * Only valid values will be changed.
     * 
     * @param companyId unique identifier of company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param unitId partially unique identifier of the Unit
     * @param unitDto contains the updated data, that will be changed
     * @return the corresponding UnitDto
     * @throws UnitNotFoundException if the Unit was not found
     */
    UnitDto patchUnit(long companyId, long businessUnitId, long unitId, UnitDto unitDto) throws UnitNotFoundException;



    /**
     * Deletes a Unit from a repository based on its ID.
     * 
     * If the Unit was not found it is silently ignored.
     * 
     * @param companyId unique identifier of company
     * @param businessUnitId partially unique identifier of the BusinessUnit
     * @param unitId partially unique identifier of the Unit
     * 
     * @see UnitId
     */
    void deleteUnit(long companyId, long businessUnitId, long unitId);





}
