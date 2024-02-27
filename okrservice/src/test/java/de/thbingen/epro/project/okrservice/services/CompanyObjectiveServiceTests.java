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

import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.services.impl.CompanyObjectiveServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CompanyObjectiveServiceTests {
    @InjectMocks
    private CompanyObjectiveServiceImpl companyObjectiveService;
    @Mock
    private CompanyService companyService;
    @Mock
    private CompanyObjectiveRepository companyObjectiveRepository;
    @Mock
    private UserService userService;

    private User user;
    private Company company;
    private List<CompanyObjective> companyObjectives;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur")
                .companies(new ArrayList<>())
                .build();

        company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        user.addCompany(company);
        companyObjectives = new ArrayList<>();
        company.setObjectives(companyObjectives);
    }

    @Test
    public void CompanyObjectiveService_CreateObjective_ReturnCompanyObjectiveDto() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .company(company)
                .build();

        CompanyObjectiveDto companyObjectiveDto = companyObjective.toDto();

        when(userService.findUser(user.getId())).thenReturn(user);
        when(companyService.findCompany(company.getId())).thenReturn(company);

        CompanyObjectiveDto createdCompanyObjectiveDto = companyObjectiveService.createObjective(1L, companyObjectiveDto);

        Assertions.assertThat(createdCompanyObjectiveDto).isEqualTo(companyObjectiveDto);
    }

    @Test
    public void CompanyObjectiveService_FindAllObjectives_ReturnCompanyObjectiveDtoList() {
        for(long i = 1L; i<3; i++) {
            CompanyObjective companyObjective = CompanyObjective.builder()
                    .id(i)
                    .deadline(Instant.now())
                    .title("company objective 01")
                    .description("It's a company objective")
                    .owner(user)
                    .build();
            companyObjectives.add(companyObjective);
        }

        ArrayList<CompanyObjectiveDto> companyObjectiveDtos = companyObjectives.stream()
                .map(CompanyObjectiveDto::new).collect(Collectors.toCollection(ArrayList::new));

        when(companyObjectiveRepository.findByCompanyId(company.getId())).thenReturn(companyObjectives.stream().collect(Collectors.toList()));

        List<CompanyObjectiveDto> foundCompanyObjectiveDtos =
                companyObjectiveService.findAllObjectives(company.getId());

        Assertions.assertThat(foundCompanyObjectiveDtos).isEqualTo(companyObjectiveDtos);
    }

    @Test
    public void CompanyObjectiveService_FindObjective_ReturnCompanyObjective() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .company(company)
                .build();

        when(companyObjectiveRepository.findById(companyObjective.getId())).thenReturn(Optional.of(companyObjective));

        CompanyObjective foundCompanyObjective = companyObjectiveService.findObjective(companyObjective.getId());

        Assertions.assertThat(foundCompanyObjective).isEqualTo(companyObjective);
    }

    @Test
    public void CompanyObjectiveService_PatchObjective_ReturnCompanyObjectiveDto() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .company(company)
                .build();

        CompanyObjectiveDto companyObjectiveDto = companyObjective.toDto();

        when(userService.findUser(user.getId())).thenReturn(user);
        when(companyObjectiveRepository.findById(companyObjective.getId())).thenReturn(Optional.of(companyObjective));

        CompanyObjectiveDto patchedCompanyObjectiveDto =
                companyObjectiveService.patchObjective(1L, companyObjectiveDto);

        Assertions.assertThat(patchedCompanyObjectiveDto).isEqualTo(companyObjectiveDto);
    }

    @Test
    public void CompanyObjectiveService_DeleteObjective_ReturnCompanyObjectiveDto() {
        assertAll(() -> companyObjectiveService.deleteObjective(1L));
    }
}
