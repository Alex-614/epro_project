package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitRepository;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessUnitServiceTests {
    @InjectMocks
    private BusinessUnitServiceImpl businessUnitService;
    @Mock
    private CompanyService companyService;
    @Mock
    private BusinessUnitRepository businessUnitRepository;

    @Test
    public void BusinessUnitService_CreateBusinessUnit_ReturnBusinessUnitDto() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(company)
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);

        when(companyService.findCompany(company.getId())).thenReturn(company);

        BusinessUnitDto createdBusinessUnitDto =
                businessUnitService.createBusinessUnit(company.getId(), businessUnitDto);

        Assertions.assertThat(createdBusinessUnitDto).isEqualTo(businessUnitDto);
    }

    @Test
    public void BusinessUnitService_FindAllBusinessUnits_ReturnBusinessUnitDtoList() {
        ArrayList<BusinessUnit> businessUnits = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            BusinessUnit businessUnit = BusinessUnit.builder()
                    .id(new BusinessUnitId(i, 1L))
                    .company(new Company())
                    .name("BU01")
                    .objectives(new ArrayList<>())
                    .build();
            businessUnits.add(businessUnit);
        }

        ArrayList<BusinessUnitDto> businessUnitDtos = businessUnits.stream()
                .map(BusinessUnitDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(businessUnitRepository.findByCompanyId(1L)).thenReturn(businessUnits);

        List<BusinessUnitDto> foundBusinessUnitDtos = businessUnitService.findAllBusinessUnits(1L);

        Assertions.assertThat(foundBusinessUnitDtos).isEqualTo(businessUnitDtos);
    }

    @Test
    public void BusinessUnitService_FindBusinessUnit_ReturnBusinessUnit() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        when(businessUnitRepository.findById(new BusinessUnitId(1L, 1L)))
                .thenReturn(Optional.of(businessUnit));

        BusinessUnit foundBusinessUnit = businessUnitService.findBusinessUnit(1L, 1L);

        Assertions.assertThat(foundBusinessUnit).isEqualTo(businessUnit);
    }

    @Test
    public void BusinessUnitService_PatchBusinessUnit_ReturnBusinessUnitDto() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);

        when(businessUnitRepository.findById(new BusinessUnitId(1L, 1L)))
                .thenReturn(Optional.of(businessUnit));

        BusinessUnitDto patchedBusinessUnitDto =
                businessUnitService.patchBusinessUnit(1L, 1L, businessUnitDto);

        Assertions.assertThat(patchedBusinessUnitDto).isEqualTo(businessUnitDto);
    }

    @Test
    public void BusinessUnitService_DeleteBusinessUnit_ReturnBusinessUnitDto() {
        assertAll(() -> businessUnitService.deleteBusinessUnit(1L, 1L));
    }
}
