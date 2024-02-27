package de.thbingen.epro.project.okrservice.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultTypeRepository;
import de.thbingen.epro.project.okrservice.repositories.KeyResultUpdateRepository;
import de.thbingen.epro.project.okrservice.services.impl.CompanyKeyResultServiceImpl;
import de.thbingen.epro.project.okrservice.services.impl.CompanyObjectiveServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CompanyKeyResultServiceTests {
    @InjectMocks
    private CompanyKeyResultServiceImpl companyKeyResultService;
    @Mock
    private CompanyKeyResultRepository companyKeyResultRepository;
    @Mock
    private KeyResultTypeRepository keyResultTypeRepository;
    @Mock
    private BusinessUnitObjectiveService businessUnitObjectiveService;
    @Mock
    private CompanyObjectiveServiceImpl companyObjectiveService;
    @Mock
    private UserService userService;
    @Mock
    private KeyResultUpdateRepository keyResultUpdateRepository;

    @Test
    public void CompanyKeyResultService_CreateKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(CompanyObjective.builder().id(1L).build())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .representers(new HashSet<>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = new CompanyKeyResultDto(companyKeyResult);

        when(companyObjectiveService.findObjective(1L)).thenReturn(CompanyObjective.builder().id(1L).keyReslts(new HashSet<>()).build());
        when(keyResultTypeRepository.findByName("numeric")).thenReturn(Optional.of(new KeyResultType("numeric")));

        CompanyKeyResultDto createdCompanyKeyResultDto =
                companyKeyResultService.createKeyResult(1L, companyKeyResultDto);

        Assertions.assertThat(createdCompanyKeyResultDto).isEqualTo(companyKeyResultDto);
    }

    @Test
    public void CompanyKeyResultService_FindAllKeyResults_ReturnCompanyKeyResultDtoList() {
        ArrayList<CompanyKeyResult> companyKeyResults = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                    .goal(10000)
                    .title("test key result")
                    .description("it's a test key result")
                    .current(500)
                    .confidenceLevel(20)
                    .objective(new CompanyObjective())
                    .type(new KeyResultType("numeric"))
                    .lastUpdate(new KeyResultUpdate())
                    .contributingUnits(new HashSet<>())
                    .contributingBusinessUnits(new HashSet<>())
                    .representers(new HashSet<>())
                    .build();
            companyKeyResults.add(companyKeyResult);
        }

        ArrayList<CompanyKeyResultDto> companyKeyResultDtos = companyKeyResults.stream()
                .map(CompanyKeyResultDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(companyKeyResultRepository.findByObjectiveId(1L)).thenReturn(companyKeyResults);

        List<CompanyKeyResultDto> foundCompanyKeyResultDtos = companyKeyResultService.findAllKeyResults(1L);

        Assertions.assertThat(foundCompanyKeyResultDtos).isEqualTo(companyKeyResultDtos);
    }

    @Test
    public void CompanyKeyResultService_FindKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .representers(new HashSet<>())
                .build();

        when(companyKeyResultRepository.findById(1L)).thenReturn(Optional.ofNullable(companyKeyResult));

        CompanyKeyResult foundCompanyKeyResult = companyKeyResultService.findKeyResult(1L);

        Assertions.assertThat(foundCompanyKeyResult).isEqualTo(companyKeyResult);
    }

    @Test
    public void CompanyKeyResultService_FindContributingBusinessUnits_ReturnBusinessUnitDtoList() throws Exception {
        Set<BusinessUnit> businessUnits = new HashSet<>();
        businessUnits.add(new BusinessUnit());

        List<BusinessUnitDto> businessUnitDtos = new ArrayList<>();
        businessUnitDtos.add(new BusinessUnitDto());

        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .representers(new HashSet<>())
                .contributingBusinessUnits(businessUnits)
                .build();

        when(companyKeyResultRepository.findById(1L)).thenReturn(Optional.ofNullable(companyKeyResult));

        List<BusinessUnitDto> foundBusinessUnitDtos = companyKeyResultService.findContributingBusinessUnits(1L);

        Assertions.assertThat(foundBusinessUnitDtos).isEqualTo(businessUnitDtos);
    }

    @Test
    public void CompanyKeyResultService_FindContributingUnits_ReturnUnitDtoList() throws Exception {
        Set<Unit> units = new HashSet<>();
        units.add(new Unit());

        List<UnitDto> unitDtos = new ArrayList<>();
        unitDtos.add(new UnitDto());

        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .representers(new HashSet<>())
                .contributingUnits(units)
                .build();

        when(companyKeyResultRepository.findById(1L)).thenReturn(Optional.of(companyKeyResult));

        List<UnitDto> foundUnitDtos = companyKeyResultService.findContributingUnits(1L);

        Assertions.assertThat(foundUnitDtos).isEqualTo(unitDtos);
    }

    //toDo: add findKeyResultHistory test
    /*@Test
    public void CompanyKeyResultService_FindKeyResultHistory_ReturnUnitDtoList() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(new CompanyObjective())
                .type(new KeyResultType("numeric"))
                .contributingUnits(new ArrayList<Unit>())
                .contributingBusinessUnits(new ArrayList<BusinessUnit>())
                .representers(new ArrayList<BusinessUnitObjective>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = companyKeyResult.toDto();

        User user = User.builder()
                .id(1L).build();

        KeyResultUpdate keyResultUpdate = new KeyResultUpdate("update", Instant.ofEpochSecond(1233123345L),
                companyKeyResult, companyKeyResult, companyKeyResult, user);

        keyResultUpdate.setOldKeyResult(null);

        companyKeyResult.setLastUpdate(keyResultUpdate);

        LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>> updateHistory = new LinkedList<>();
        KeyResultUpdateDto<CompanyKeyResultDto> keyResultDtoKeyResultUpdateDto =
                new KeyResultUpdateDto<>("update", 1233123345L, 1L,
                        companyKeyResultDto, companyKeyResultDto, companyKeyResultDto);
        keyResultDtoKeyResultUpdateDto.setOldKeyResult(null);
        updateHistory.add(keyResultDtoKeyResultUpdateDto);

        when(companyKeyResultRepository.findById(1L)).thenReturn(Optional.of(companyKeyResult));

        LinkedList<KeyResultUpdateDto<CompanyKeyResultDto>> foundUpdateHistory =
                companyKeyResultService.findKeyResultUpdateHistory(1L);

        Assertions.assertThat(foundUpdateHistory).isEqualTo(updateHistory);
    }*/

    @Test
    public void CompanyKeyResultService_PatchKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
                .goal(10000)
                .title("test key result")
                .description("it's a test key result")
                .current(500)
                .confidenceLevel(20)
                .objective(CompanyObjective.builder().id(1L).build())
                .type(new KeyResultType("numeric"))
                .lastUpdate(new KeyResultUpdate())
                .contributingUnits(new HashSet<>())
                .contributingBusinessUnits(new HashSet<>())
                .representers(new HashSet<>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = companyKeyResult.toDto();

        KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto = new KeyResultPatchDto<>("update", 124412345L,
                1L, companyKeyResultDto);

        when(companyKeyResultRepository.findById(1L)).thenReturn(Optional.of(companyKeyResult));
        when(keyResultTypeRepository.findByName("numeric")).thenReturn(Optional.of(new KeyResultType("numeric")));

        when(companyKeyResultRepository.save(Mockito.any()))
                .thenReturn(companyKeyResult.getId() != null ? companyKeyResult : CompanyKeyResult.builder().id(2L).build());
        
        CompanyKeyResultDto patchedCompanyObjectiveDto =
                companyKeyResultService.patchKeyResult(1L, keyResultPatchDto);

        Assertions.assertThat(patchedCompanyObjectiveDto).isEqualTo(companyKeyResultDto);
    }

    @Test
    public void CompanyKeyResultService_DeleteKeyResult_ReturnVoid() {
        assertAll(() -> companyKeyResultService.deleteKeyResult(1L));
    }
}
