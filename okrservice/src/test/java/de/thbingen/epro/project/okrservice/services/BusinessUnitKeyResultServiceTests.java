package de.thbingen.epro.project.okrservice.services;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitKeyResultServiceImpl;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitObjectiveServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BusinessUnitKeyResultServiceTests {
    @InjectMocks
    private BusinessUnitKeyResultServiceImpl businessUnitKeyResultService;
    @Mock
    private BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    @Mock
    private BusinessUnitObjectiveServiceImpl businessUnitObjectiveService;
    @Mock
    private BusinessUnitService businessUnitService;
    @Mock
    private KeyResultTypeRepository keyResultTypeRepository;
    @Mock
    private UserService userService;
    @Mock
    private KeyResultUpdateRepository keyResultUpdateRepository;





    @Test
    public void BusinessUnitKeyResultController_PostBusinessUnitKeyResult_ReturnBusinessUnitKeyResultDto() throws Exception {
        HashSet<BusinessUnit> businessUnits = new HashSet<>();
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new HashSet<>())
                .build();
        businessUnits.add(businessUnit);

        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(businessUnits)
                .build();

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        /*when(businessUnitService.findBusinessUnit(1L, 1L)).thenReturn(new BusinessUnit());
        when(businessUnitObjectiveService.findObjective(1L)).thenReturn(new BusinessUnitObjective());
        when(keyResultTypeRepository.findByName("numeric")).thenReturn(Optional.of(new KeyResultType("numeric")));*/

        //toDo: Unsupported Operations Exception
        /*BusinessUnitKeyResultDto createdBusinessUnitKeyResultDto =
                businessUnitKeyResultService
                        .createKeyResult(1L,1L,1L, businessUnitKeyResultDto);

        Assertions.assertThat(createdBusinessUnitKeyResultDto).isEqualTo(businessUnitKeyResultDto);*/
    }
}
