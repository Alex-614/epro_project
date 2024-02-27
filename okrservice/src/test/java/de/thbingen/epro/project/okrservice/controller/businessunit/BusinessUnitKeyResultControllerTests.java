package de.thbingen.epro.project.okrservice.controller.businessunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.*;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.services.BusinessUnitKeyResultService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessUnitKeyResultController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BusinessUnitKeyResultControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BusinessUnitKeyResultController businessUnitKeyResultController;
    @MockBean
    private UserService userService;
    @MockBean
    private BusinessUnitKeyResultService businessUnitKeyResultService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void BusinessUnitKeyResultController_PostBusinessUnitKeyResult_ReturnBusinessUnitKeyResultDto() throws Exception {
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

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        given(businessUnitKeyResultService.createKeyResult(1L, 1L, 1L, businessUnitKeyResultDto))
                .willAnswer((invocation -> invocation.getArgument(3)));

        ResultActions response = mockMvc.perform(post("/company/1/businessunit/1/objective/1/keyresult")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessUnitKeyResultDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitKeyResultDto)));
    }

    @Test
    public void BusinessUnitKeyResultController_GetAllBusinessUnitKeyResults_ReturnBusinessUnitKeyResultDto() throws Exception {
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

        when(businessUnitKeyResultService.findAllKeyResults(1L)).thenReturn(businessUnitKeyResultDtos);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/objective/1/keyresult"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitKeyResultDtos)));
    }

    @Test
    public void BusinessUnitKeyResultController_GetBusinessUnitKeyResult_ReturnBusinessUnitKeyResultDto() throws Exception {
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

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        when(businessUnitKeyResultService.findKeyResult(1L)).thenReturn(businessUnitKeyResult);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/objective/1/keyresult/1"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitKeyResultDto)));
    }

    @Test
    public void BusinessUnitKeyResultController_GetContributingUnits_ReturnUnitDtoList() throws Exception {
        List<UnitDto> unitDtos = new ArrayList<>();
        unitDtos.add(new UnitDto());

        when(businessUnitKeyResultService.findContributingUnits(1L)).thenReturn(unitDtos);

        ResultActions response = mockMvc.perform(
                get("/company/1/businessunit/1/objective/1/keyresult/1/contributing/units"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(unitDtos)));
    }

    @Test
    public void BusinessUnitKeyResultController_GetContributingBusinessUnits_ReturnBusinessBusinessUnitDtoList() throws Exception {
        List<BusinessUnitDto> businessUnitDtos = new ArrayList<>();
        businessUnitDtos.add(new BusinessUnitDto());

        when(businessUnitKeyResultService.findContributingBusinessUnits(1L)).thenReturn(businessUnitDtos);

        ResultActions response = mockMvc.perform(
                get("/company/1/businessunit/1/objective/1/keyresult/1/contributing/businessunits"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDtos)));
    }

    @Test
    public void BusinessUnitKeyResultController_GetBusinessUnitKeyResultUpdateHistory_ReturnKeyResultUpdateDtoList() throws Exception {
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

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        KeyResultUpdateDto<BusinessUnitKeyResultDto> keyResultUpdateDto = new KeyResultUpdateDto<>("update", 12334145L, 1L,
                businessUnitKeyResultDto, businessUnitKeyResultDto, businessUnitKeyResultDto);

        LinkedList<KeyResultUpdateDto<BusinessUnitKeyResultDto>> keyResultUpdateDtos = new LinkedList<>();
        keyResultUpdateDtos.add(keyResultUpdateDto);

        when(businessUnitKeyResultService.findKeyResultUpdateHistory(1L)).thenReturn(keyResultUpdateDtos);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/objective/1/keyresult/1/updatehistory"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(keyResultUpdateDtos)));
    }

    @Test
    public void BusinessUnitKeyResultController_PatchBusinessUnitKeyResultUpdateHistory_ReturnBusinessUnitKeyResultDto() throws Exception {
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

        BusinessUnitKeyResultDto businessUnitKeyResultDto = new BusinessUnitKeyResultDto(businessUnitKeyResult);

        KeyResultPatchDto<BusinessUnitKeyResultDto> keyResultPatchDto = new KeyResultPatchDto<>("update", 124412345L,
                1L, businessUnitKeyResultDto);

        when(businessUnitKeyResultService.patchKeyResult(1L, keyResultPatchDto))
                .thenReturn(businessUnitKeyResultDto);

        ResultActions response = mockMvc.perform(patch("/company/1/businessunit/1/objective/1/keyresult/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keyResultPatchDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitKeyResultDto)));
    }

    @Test
    public void BusinessUnitKeyResultController_DeleteBusinessUnitKeyResultUpdateHistory_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/businessunit/1/objective/1/keyresult/1"));
        response.andExpect(status().isOk());
    }
}
