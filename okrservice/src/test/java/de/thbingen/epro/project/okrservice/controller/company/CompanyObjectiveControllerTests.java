package de.thbingen.epro.project.okrservice.controller.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.CompanyObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.services.CompanyObjectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyObjectiveController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CompanyObjectiveControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CompanyObjectiveController companyObjectiveController;
    @MockBean
    private CompanyObjectiveService companyObjectiveService;
    @Autowired
    private ObjectMapper objectMapper;

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
                .companies(new ArrayList<Company>())
                .build();

        company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        user.addCompany(company);
        companyObjectives = new ArrayList<>();
        company.setObjectives(companyObjectives);
    }

    @Test
    @WithMockUser(username = "testuser", password = "test", roles = "CO_OKR_ADMIN")
    public void CompanyObjectiveController_PostCompanyObjective_ReturnCompanyObjectiveDto() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .build();

        companyObjectives.add(companyObjective);
        companyObjective.setCompany(company);
        CompanyObjectiveDto companyObjectiveDto = new CompanyObjectiveDto(companyObjective);

        given(companyObjectiveService.createObjective(company.getId(), companyObjectiveDto)).willAnswer((invocation -> invocation.getArgument(1)));

        ResultActions response = mockMvc.perform(post("/company/{companyId}/objective", company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyObjectiveDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyObjectiveDto)));
    }

    @Test
    public void CompanyObjectiveController_GetAllCompanyObjective_ReturnResponseDto() throws Exception {
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

        when(companyObjectiveService.findAllObjectives(company.getId())).thenReturn(companyObjectiveDtos);

        ResultActions response = mockMvc.perform(get("/company/1/objective"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyObjectiveDtos)));
    }

    @Test
    public void CompanyObjectiveController_GetCompanyObjective_ReturnCompanyObjectiveDto() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .build();

        companyObjectives.add(companyObjective);
        companyObjective.setCompany(company);
        CompanyObjectiveDto companyObjectiveDto = new CompanyObjectiveDto(companyObjective);

        when(companyObjectiveService.findObjective(1L)).thenReturn(companyObjective);

        ResultActions response = mockMvc.perform(get("/company/1/objective/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyObjectiveDto)));
    }

    @Test
    public void CompanyObjectiveController_PatchCompanyObjective_ReturnCompanyObjectiveDto() throws Exception {
        CompanyObjective companyObjective = CompanyObjective.builder()
                .id(1L)
                .deadline(Instant.now())
                .title("company objective 01")
                .description("It's a company objective")
                .owner(user)
                .build();

        companyObjectives.add(companyObjective);
        companyObjective.setCompany(company);
        CompanyObjectiveDto companyObjectiveDto = new CompanyObjectiveDto(companyObjective);

        when(companyObjectiveService.patchObjective(1L, companyObjectiveDto)).thenReturn(companyObjectiveDto);

        ResultActions response = mockMvc.perform(patch("/company/1/objective/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyObjectiveDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyObjectiveDto)));
    }

    @Test
    public void CompanyObjectiveController_DeleteCompanyObjective_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/objective/1"));

        response.andExpect(status().isOk());
    }
}
