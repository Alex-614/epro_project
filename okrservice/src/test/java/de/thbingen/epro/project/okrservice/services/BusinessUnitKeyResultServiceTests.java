package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.*;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.exceptions.*;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitKeyResultServiceImpl;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitObjectiveServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
        BusinessUnit businessUnit01 = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new HashSet<>())
                .build();
        businessUnits.add(businessUnit01);
        BusinessUnit businessUnit02 = BusinessUnit.builder()
                .id(new BusinessUnitId(2L, 1L))
                .company(new Company())
                .name("BU02")
                .objectives(new HashSet<>())
                .build();
        businessUnits.add(businessUnit02);

        BusinessUnitObjective businessUnitObjective = BusinessUnitObjective.builder().id(1L).keyReslts(new HashSet<>()).build();

        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(businessUnitObjective)
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(businessUnits)
                .build();

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        when(businessUnitObjectiveService.findObjective(1L)).thenReturn(businessUnitObjective);
        when(keyResultTypeRepository.findByName("numeric")).thenReturn(Optional.of(new KeyResultType("numeric")));

        BusinessUnitKeyResultDto createdBusinessUnitKeyResultDto =
                businessUnitKeyResultService
                        .createKeyResult(1L,1L,1L, businessUnitKeyResultDto);

        Assertions.assertThat(createdBusinessUnitKeyResultDto).isEqualTo(businessUnitKeyResultDto);
    }

    @Test
    public void BusinessUnitKeyResultController_FindAllBusinessUnitKeyResults_ReturnBusinessUnitKeyResultDtoList() throws KeyResultNotFoundException {
        ArrayList<BusinessUnitKeyResult> businessUnitKeyResults = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                    .id(1L)
                    .goal(10000)
                    .title("test key result")
                    .description("it's a test key result")
                    .current(500)
                    .confidenceLevel(20)
                    .objective(new CompanyObjective())
                    .type(new KeyResultType("numeric"))
                    .lastUpdate(new KeyResultUpdate())
                    .contributingUnits(new HashSet<Unit>())
                    .contributingBusinessUnits(new HashSet<BusinessUnit>())
                    .build();
            businessUnitKeyResults.add(businessUnitKeyResult);
        }

        ArrayList<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = businessUnitKeyResults.stream()
                .map(BusinessUnitKeyResultDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(businessUnitKeyResultRepository.findByObjectiveId(1L)).thenReturn(businessUnitKeyResults);

        List<BusinessUnitKeyResultDto> foundBusinessUnitKeyResultDtos =
                businessUnitKeyResultService.findAllKeyResults(1L);

        Assertions.assertThat(foundBusinessUnitKeyResultDtos).isEqualTo(businessUnitKeyResultDtos);
    }

    @Test
    public void BusinessUnitKeyResultController_FindBusinessUnitKeyResult_ReturnBusinessUnitKeyResult() throws KeyResultNotFoundException {
        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<Unit>())
                .contributingBusinessUnits(new HashSet<BusinessUnit>())
                .build();

        when(businessUnitKeyResultRepository.findById(1L)).thenReturn(Optional.ofNullable(businessUnitKeyResult));

        BusinessUnitKeyResult foundBusinessUnitKeyResult =
                businessUnitKeyResultService.findKeyResult(1L);

        Assertions.assertThat(foundBusinessUnitKeyResult).isEqualTo(businessUnitKeyResult);
    }

    @Test
    public void BusinessUnitKeyResultController_FindContributingBusinessUnits_ReturnBusinessUnitDtoList() throws KeyResultNotFoundException {
        Set<BusinessUnit> businessUnits = new HashSet<>();
        businessUnits.add(new BusinessUnit());

        Set<BusinessUnitDto> businessUnitDtos = new HashSet<>();
        businessUnitDtos.add(new BusinessUnitDto());

        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<Unit>())
                .contributingBusinessUnits(businessUnits)
                .build();

        when(businessUnitKeyResultRepository.findById(1L)).thenReturn(Optional.ofNullable(businessUnitKeyResult));

        List<BusinessUnitDto> foundBusinessUnitDtos = businessUnitKeyResultService.findContributingBusinessUnits(1L);

        Assertions.assertThat(foundBusinessUnitDtos).containsExactlyInAnyOrderElementsOf(businessUnitDtos);
    }

    @Test
    public void BusinessUnitKeyResultController_FindContributingUnits_ReturnUnitDtoList() throws KeyResultNotFoundException {
        Set<Unit> units = new HashSet<>();
        units.add(new Unit());

        Set<UnitDto> unitDtos = new HashSet<>();
        unitDtos.add(new UnitDto());

        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(units)
                .contributingBusinessUnits(new HashSet<BusinessUnit>())
                .build();

        when(businessUnitKeyResultRepository.findById(1L)).thenReturn(Optional.ofNullable(businessUnitKeyResult));

        List<UnitDto> foundUnitDtos = businessUnitKeyResultService.findContributingUnits(1L);

        Assertions.assertThat(foundUnitDtos).containsExactlyInAnyOrderElementsOf(unitDtos);
    }

    @Test
    public void BusinessUnitKeyResultController_FindKeyResultHistory_ReturnKeyResultUpdateList() throws KeyResultNotFoundException {
        User user = User.builder().id(1L).build();

        BusinessUnitKeyResult oldBusinessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(null)
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .build();
        BusinessUnitKeyResultDto oldBusinessUnitKeyResultDto = oldBusinessUnitKeyResult.toDto();

        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(2L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .build();
        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResult.toDto();

        KeyResultUpdate keyResultUpdate =
                new KeyResultUpdate("update", Instant.ofEpochSecond(12332134L),
                        businessUnitKeyResult, oldBusinessUnitKeyResult, businessUnitKeyResult, user);

        KeyResultUpdateDto<BusinessUnitKeyResultDto> keyResultUpdateDto =
                new KeyResultUpdateDto<>("update", 12332134000L, user.getId(),
                        businessUnitKeyResultDto, oldBusinessUnitKeyResultDto, businessUnitKeyResultDto);

        businessUnitKeyResult.setLastUpdate(keyResultUpdate);

        LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>> updateHistory = new LinkedList<>();
        updateHistory.add(keyResultUpdateDto);

        when(businessUnitKeyResultRepository.findById(1L)).thenReturn(Optional.of(oldBusinessUnitKeyResult));
        when(businessUnitKeyResultRepository.findById(2L)).thenReturn(Optional.of(businessUnitKeyResult));

        LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>> foundUpdateHistory =
                businessUnitKeyResultService.findKeyResultUpdateHistory(2L);

        Assertions.assertThat(foundUpdateHistory).containsExactlyInAnyOrderElementsOf(updateHistory);
    }

    @Test
    public void BusinessUnitKeyResultController_PatchBusinessUnitKeyResult_ReturnBusinessUnitKeyResultDto() throws UserNotFoundException, KeyResultTypeNotFoundException, KeyResultDeprecatedException, KeyResultNotFoundException, ObjectiveNotFoundException {
        BusinessUnitKeyResult businessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<Unit>())
                .contributingBusinessUnits(new HashSet<BusinessUnit>())
                .build();
        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResult.toDto();

        BusinessUnitKeyResult toSaveBusinessUnitKeyResult = BusinessUnitKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(null)
                .type(new KeyResultType("numeric"))
                .lastUpdate(null)
                .contributingUnits(new HashSet<Unit>())
                .contributingBusinessUnits(new HashSet<BusinessUnit>())
                .build();

        BusinessUnitKeyResult copyBusinessUnitKeyResult = BusinessUnitKeyResult.builder()
                .id(1L)
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<Unit>())
                .contributingBusinessUnits(new HashSet<BusinessUnit>())
                .build();

        KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto = new KeyResultPatchDto<>("update", 124412345L,
                1L, businessUnitKeyResultDto);

        when(businessUnitKeyResultRepository.findById(1L)).thenReturn(Optional.of(businessUnitKeyResult));
        when(businessUnitKeyResultRepository.save(toSaveBusinessUnitKeyResult)).thenReturn(copyBusinessUnitKeyResult);
        when(businessUnitKeyResultRepository.save(businessUnitKeyResult)).thenReturn(businessUnitKeyResult);
        when(keyResultTypeRepository.findByName("numeric")).thenReturn(Optional.of(new KeyResultType("numeric")));

        BusinessUnitKeyResultDto patchedBusinessUnitKeyResultDto =
                businessUnitKeyResultService.patchKeyResult(1L, keyResultPatchDto);

        Assertions.assertThat(patchedBusinessUnitKeyResultDto).isEqualTo(businessUnitKeyResultDto);
    }

    @Test
    public void BusinessUnitKeyResultController_DeleteBusinessUnitKeyResult_ReturnVoid() {
        assertAll(() -> businessUnitKeyResultService.deleteKeyResult(1L));
    }
}
