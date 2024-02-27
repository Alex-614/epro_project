package de.thbingen.epro.project.okrservice.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.repositories.BusinessUnitObjectiveRepository;
import de.thbingen.epro.project.okrservice.services.impl.BusinessUnitObjectiveServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BusinessUnitObjectiveServiceTests {
    @InjectMocks
    private BusinessUnitObjectiveServiceImpl businessUnitObjectiveService;
    @Mock
    private BusinessUnitService businessUnitService;
    @Mock
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = User.builder()
            .id(1L)
            .build();
    }

    @Test
    public void BusinessUnitObjectiveService_CreateObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
        BusinessUnitObjective businessUnitObjective = BusinessUnitObjective.builder()
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .keyReslts(new ArrayList<>())
                .businessUnit(new BusinessUnit())
                .represented(new ArrayList<>())
                .build();

        ArrayList<BusinessUnitObjective> businessUnitObjectives = new ArrayList<>();
        businessUnitObjectives.add(businessUnitObjective);
        BusinessUnit businessUnit = BusinessUnit.builder()
                .objectives(businessUnitObjectives)
                .build();

        BusinessUnitObjectiveDto businessUnitObjectiveDto = new BusinessUnitObjectiveDto(businessUnitObjective);

        when(businessUnitService.findBusinessUnit(1L, 1L)).thenReturn(businessUnit);
        when(userService.findUser(1L)).thenReturn(user);

        BusinessUnitObjectiveDto createdBusinessUnitObjectiveDto =
            businessUnitObjectiveService.createObjective(1L, 1L, businessUnitObjectiveDto);

        Assertions.assertThat(createdBusinessUnitObjectiveDto).isEqualTo(businessUnitObjectiveDto);
    }

    @Test
    public void BusinessUnitObjectiveService_FindAllObjectives_ReturnBusinessUnitObjectiveDtoList() {
        ArrayList<BusinessUnitObjective> businessUnitObjectives = new ArrayList<>();
        for(long i = 0L; i<3; i++) {
            BusinessUnitObjective businessUnitObjective = BusinessUnitObjective.builder()
                    .id(i)
                    .deadline(Instant.now())
                    .title("company objective 01")
                    .description("It's a company objective")
                    .owner(user)
                    .keyReslts(new ArrayList<>())
                    .businessUnit(new BusinessUnit())
                    .represented(new ArrayList<>())
                    .build();
            businessUnitObjectives.add(businessUnitObjective);
        }

        ArrayList<BusinessUnitObjectiveDto> businessUnitObjectiveDtos = businessUnitObjectives.stream()
                .map(BusinessUnitObjectiveDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(businessUnitObjectiveRepository.findByBusinessUnitId(new BusinessUnitId(1L, 1L)))
                .thenReturn(businessUnitObjectives);

        List<BusinessUnitObjectiveDto> foundBusinessUnitObjectiveDtos =
                businessUnitObjectiveService.findAllObjectives(1L, 1L);

        Assertions.assertThat(foundBusinessUnitObjectiveDtos).isEqualTo(businessUnitObjectiveDtos);
    }

    @Test
    public void BusinessUnitObjectiveService_FindBusinessUnitObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
        BusinessUnitObjective businessUnitObjective = BusinessUnitObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .keyReslts(new ArrayList<>())
                .businessUnit(new BusinessUnit())
                .represented(new ArrayList<>())
                .build();

        when(businessUnitObjectiveRepository.findById(1L)).thenReturn(Optional.ofNullable(businessUnitObjective));

        BusinessUnitObjective foundBusinessUnitObjectiveDto =
                businessUnitObjectiveService.findObjective(1L);

        Assertions.assertThat(foundBusinessUnitObjectiveDto).isEqualTo(businessUnitObjective);
    }

    @Test
    public void BusinessUnitObjectiveService_PatchBusinessUnitObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
        BusinessUnitObjective businessUnitObjective = BusinessUnitObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .keyReslts(new ArrayList<>())
                .businessUnit(new BusinessUnit())
                .represented(new ArrayList<>())
                .build();

        BusinessUnitObjectiveDto businessUnitObjectiveDto = new BusinessUnitObjectiveDto(businessUnitObjective);

        when(businessUnitObjectiveRepository.findById(1L)).thenReturn(Optional.ofNullable(businessUnitObjective));

        BusinessUnitObjectiveDto patchedBusinessUnitObjectiveDto =
                businessUnitObjectiveService.patchObjective(1L, businessUnitObjectiveDto);

        Assertions.assertThat(patchedBusinessUnitObjectiveDto).isEqualTo(businessUnitObjectiveDto);
    }

    @Test
    public void CompanyObjectiveService_DeleteObjective_ReturnCompanyObjectiveDto() {
        assertAll(() -> businessUnitObjectiveService.deleteObjective(1L));
    }
}
