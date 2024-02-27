package de.thbingen.epro.project.okrservice.controller.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultPatchDto;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.services.CompanyKeyResultService;
import de.thbingen.epro.project.okrservice.services.UserService;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyKeyResultController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CompanyKeyResultControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CompanyKeyResultController companyKeyResultController;
    @MockBean
    private UserService userService;
    @MockBean
    private CompanyKeyResultService companyKeyResultService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", password = "test", roles = "CO_OKR_ADMIN")
    public void CompanyKeyResultController_PostCompanyKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
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
                .representers(new HashSet<BusinessUnitObjective>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = new CompanyKeyResultDto(companyKeyResult);


        given(companyKeyResultService.createKeyResult(1L, companyKeyResultDto)).willAnswer((invocation -> invocation.getArgument(1)));

        ResultActions response = mockMvc.perform(post("/company/1/objective/1/keyresult")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyKeyResultDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyKeyResultDto)));
    }

    @Test
    public void CompanyKeyResultController_GetCompanyKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
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
                .representers(new HashSet<BusinessUnitObjective>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = new CompanyKeyResultDto(companyKeyResult);

        when(companyKeyResultService.findKeyResult(1L)).thenReturn(companyKeyResult);

        ResultActions response = mockMvc.perform(get("/company/1/objective/1/keyresult/1"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyKeyResultDto)));
    }

    @Test
    public void CompanyKeyResultController_GetContributingUnits_ReturnUnitDtoList() throws Exception {;
        List<UnitDto> unitDtos = new ArrayList<>();
        unitDtos.add(new UnitDto());

        when(companyKeyResultService.findContributingUnits(1L)).thenReturn(unitDtos);

        ResultActions response = mockMvc.perform(get("/company/1/objective/1/keyresult/1/contributing/units"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(unitDtos)));
    }

    @Test
    public void CompanyKeyResultController_GetContributingBusinessUnits_ReturnBusinessUnitDtoList() throws Exception {
        List<BusinessUnitDto> businessUnitDtos = new ArrayList<>();
        businessUnitDtos.add(new BusinessUnitDto());

        when(companyKeyResultService.findContributingBusinessUnits(1L)).thenReturn(businessUnitDtos);

        ResultActions response = mockMvc.perform(get("/company/1/objective/1/keyresult/1/contributing/businessunits"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDtos)));
    }

    @Test
    public void CompanyKeyResultController_PatchCompanyKeyResult_ReturnCompanyKeyResultDto() throws Exception {
        CompanyKeyResult companyKeyResult = CompanyKeyResult.builder()
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
                .representers(new HashSet<BusinessUnitObjective>())
                .build();

        CompanyKeyResultDto companyKeyResultDto = new CompanyKeyResultDto(companyKeyResult);

        KeyResultPatchDto<CompanyKeyResultDto> keyResultPatchDto = new KeyResultPatchDto<>("update", 124412345L,
                1L, companyKeyResultDto);

        when(companyKeyResultService.patchKeyResult(1L, keyResultPatchDto)).thenReturn(companyKeyResultDto);
        
        ResultActions response = mockMvc.perform(patch("/company/1/objective/1/keyresult/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keyResultPatchDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyKeyResultDto)));
    }

    @Test
    public void CompanyKeyResultController_DeleteCompanyKeyResult_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/objective/1/keyresult/1"));

        response.andExpect(status().isOk());
    }
}
