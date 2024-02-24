package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;

public interface BusinessUnitService {


    BusinessUnitDto createBusinessUnit(long companyId, BusinessUnitDto businessUnitDto) throws CompanyNotFoundException, BusinessUnitAlreadyExistsException;
    List<BusinessUnitDto> findAllBusinessUnits(long companyId);
    BusinessUnit findBusinessUnit(long companyId, long businessUnitId) throws BusinessUnitNotFoundException;
    BusinessUnitDto patchBusinessUnit(long companyId, long businessUnitId, BusinessUnitDto businessUnitDto) throws BusinessUnitNotFoundException;
    void deleteBusinessUnit(long companyId, long businessUnitId);


}
