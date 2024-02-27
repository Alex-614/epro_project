package de.thbingen.epro.project.okrservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
import de.thbingen.epro.project.okrservice.services.CompanyService;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private CompanyService companyService;

    private BusinessUnitRepository businessUnitRepository;


    @Autowired
    public BusinessUnitServiceImpl(CompanyService companyService, BusinessUnitRepository businessUnitRepository) {
        this.companyService = companyService;
        this.businessUnitRepository = businessUnitRepository;
    }


    @Override
    public BusinessUnitDto createBusinessUnit(long companyId, BusinessUnitDto businessUnitDto) throws CompanyNotFoundException, BusinessUnitAlreadyExistsException {
        Company company = companyService.findCompany(companyId);

        if (businessUnitRepository.existsByNameAndCompanyIdEquals(businessUnitDto.getName(), company.getId())) {
            throw new BusinessUnitAlreadyExistsException();
        }

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setName(businessUnitDto.getName());
        businessUnit.setCompany(company);
        
        businessUnitRepository.save(businessUnit);

        businessUnitDto.setId(businessUnit.getId());
        return businessUnitDto;
    }

    @Override
    public List<BusinessUnitDto> findAllBusinessUnits(long companyId) {
        List<BusinessUnit> businessUnits = businessUnitRepository.findByCompanyId(companyId);
        return businessUnits.stream().map(m -> m.toDto()).collect(Collectors.toList());
    }

    @Override
    public BusinessUnit findBusinessUnit(long companyId, long businessUnitId) throws BusinessUnitNotFoundException {
        BusinessUnit businessUnit = businessUnitRepository.findById(new BusinessUnitId(businessUnitId, companyId)).orElseThrow(() -> new BusinessUnitNotFoundException());
        return businessUnit;
    }

    @Override
    public BusinessUnitDto patchBusinessUnit(long companyId, long businessUnitId, BusinessUnitDto businessUnitDto) throws BusinessUnitNotFoundException {
        BusinessUnit businessUnit = findBusinessUnit(companyId, businessUnitId);

        if (businessUnitDto.getName() != null && !businessUnitDto.getName().isBlank()) 
            if (!businessUnitRepository.existsByNameAndCompanyIdEquals(businessUnitDto.getName(), companyId))
                businessUnit.setName(businessUnitDto.getName());

        businessUnitRepository.save(businessUnit);
        return businessUnit.toDto();
    }

    @Override
    public void deleteBusinessUnit(long companyId, long businessUnitId) {
        businessUnitRepository.deleteById(new BusinessUnitId(businessUnitId, companyId));
    }














    
}
