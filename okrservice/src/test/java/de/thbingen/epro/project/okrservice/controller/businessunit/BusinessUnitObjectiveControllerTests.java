package de.thbingen.epro.project.okrservice.controller.businessunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.services.BusinessUnitObjectiveService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessUnitObjectiveController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BusinessUnitObjectiveControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BusinessUnitObjectiveController companyObjectiveController;
    @MockBean
    private BusinessUnitObjectiveService businessUnitObjectiveService;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

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
    }

    @Test
    public void BusinessUnitObjectiveController_PostBusinessUnitObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
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

        given(businessUnitObjectiveService.createObjective(1L, 1L, businessUnitObjectiveDto))
                .willAnswer((invocation -> invocation.getArgument(2)));

        ResultActions response = mockMvc.perform(post("/company/1/businessunit/1/objective")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessUnitObjectiveDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitObjectiveDto)));
    }

    @Test
    public void BusinessUnitObjectiveController_GetAllBusinessUnitsObjective_ReturnResponseDto() throws Exception {
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

        when(businessUnitObjectiveService.findAllObjectives(1L, 1L)).thenReturn(businessUnitObjectiveDtos);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/objective"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitObjectiveDtos)));
    }

    @Test
    public void BusinessUnitObjectiveController_GetBusinessUnitObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
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

        when(businessUnitObjectiveService.findObjective(1L)).thenReturn(businessUnitObjective);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/objective/1"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitObjectiveDto)));
    }

    @Test
    public void BusinessUnitObjectiveController_PatchBusinessUnitObjective_ReturnBusinessUnitObjectiveDto() throws Exception {
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

        given(businessUnitObjectiveService.patchObjective(1L, businessUnitObjectiveDto))
                .willAnswer((invocation -> invocation.getArgument(1)));

        ResultActions response = mockMvc.perform(patch("/company/1/businessunit/1/objective/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessUnitObjectiveDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitObjectiveDto)));
    }

    @Test
    public void BusinessUnitObjectiveController_DeleteBusinessUnitObjective_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/businessunit/1/objective/1"));

        response.andExpect(status().isOk());
    }
}
