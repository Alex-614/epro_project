package de.thbingen.epro.project.okrservice.controller.businessunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.services.BusinessUnitService;
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
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessUnitController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BusinessUnitControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BusinessUnitController businessUnitController;
    @MockBean
    private BusinessUnitService businessUnitService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void BusinessUnitController_PostBusinessUnit_ReturnBusinessUnitDto() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);

        given(businessUnitService.createBusinessUnit(1L, businessUnitDto)).willAnswer((invocation -> invocation.getArgument(1)));

        ResultActions response = mockMvc.perform(post("/company/1/businessunit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessUnitDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDto)));
    }

    @Test
    public void BusinessUnitController_GetAllBusinessUnits_ReturnResponseDto() throws Exception {
        ArrayList<BusinessUnit> businessUnits = new ArrayList<>();
        for(long i = 1L; i < 3L; i++) {
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

        when(businessUnitService.findAllBusinessUnits(1L)).thenReturn(businessUnitDtos);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDtos)));
    }

    @Test
    public void BusinessUnitController_GetBusinessUnit_ReturnBusinessUnitDto() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);

        when(businessUnitService.findBusinessUnit(1L, 1L)).thenReturn(businessUnit);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDto)));
    }

    @Test
    public void BusinessUnitController_PatchBusinessUnit_ReturnBusinessUnitDto() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .company(new Company())
                .name("BU01")
                .objectives(new ArrayList<>())
                .build();

        BusinessUnitDto businessUnitDto = new BusinessUnitDto(businessUnit);

        given(businessUnitService.patchBusinessUnit(1L, 1L, businessUnitDto))
                .willAnswer((invocation -> invocation.getArgument(2)));

        ResultActions response = mockMvc.perform(patch("/company/1/businessunit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(businessUnitDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(businessUnitDto)));
    }

    @Test
    public void BusinessUnitController_DeleteBusinessUnit_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/businessunit/1"));

        response.andExpect(status().isOk());
    }
}
