package de.thbingen.epro.project.okrservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.services.UnitService;
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

@WebMvcTest(UnitController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UnitControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UnitController unitController;
    @MockBean
    private UnitService unitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void UnitController_CreateUnit_ReturnCreated() throws Exception {
        Unit unit = Unit.builder()
                .id(new UnitId(1L, new BusinessUnitId(1L, 1L)))
                .businessUnit(new BusinessUnit())
                .name("test unit")
                .build();
        UnitDto unitDto = new UnitDto(unit);

        given(unitService.createUnit(1L, 1L, unitDto)).willAnswer((invocation -> invocation.getArgument(2)));

        ResultActions response = mockMvc.perform(post("/company/1/businessunit/1/unit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(unitDto)));
    }

    @Test
    public void UnitController_GetAllUnits_ReturnResponseDto() throws Exception {
        ArrayList<Unit> units = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            Unit unit = Unit.builder()
                    .id(new UnitId(i, new BusinessUnitId(1L, 1L)))
                    .businessUnit(new BusinessUnit())
                    .name("test unit")
                    .build();
            units.add(unit);
        }

        ArrayList<UnitDto> usersDtos = units.stream()
                .map(Unit::toDto).collect(Collectors.toCollection(ArrayList::new));

        when(unitService.findAllUnits(1L, 1L)).thenReturn(usersDtos);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/unit"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(usersDtos)));
    }

    @Test
    public void UnitController_GetUnit_ReturnUnitDto() throws Exception {
        Unit unit = Unit.builder()
                .id(new UnitId(1L, new BusinessUnitId(1L, 1L)))
                .businessUnit(new BusinessUnit())
                .name("test unit")
                .build();

        UnitDto unitDto = new UnitDto(unit);

        when(unitService.findUnit(1L, 1L, 1L)).thenReturn(unit);

        ResultActions response = mockMvc.perform(get("/company/1/businessunit/1/unit/1"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(unitDto)));
    }

    @Test
    public void UnitController_PatchUnit_ReturnUnitDto() throws Exception {
        Unit unit = Unit.builder()
                .id(new UnitId(1L, new BusinessUnitId(1L, 1L)))
                .businessUnit(new BusinessUnit())
                .name("test unit")
                .build();
        UnitDto unitDto = new UnitDto(unit);

        given(unitService.patchUnit(1L, 1L, 1L, unitDto))
                .willAnswer((invocation -> invocation.getArgument(3)));

        ResultActions response = mockMvc.perform(patch("/company/1/businessunit/1/unit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(unitDto)));
    }

    @Test
    public void UnitController_DeleteUnit_ReturnHttpStatus() throws Exception {
        ResultActions response = mockMvc.perform(delete("/company/1/businessunit/1/unit/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
