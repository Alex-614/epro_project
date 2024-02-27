package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.UnitDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.exceptions.BusinessUnitNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UnitAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UnitNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.UnitRepository;
import de.thbingen.epro.project.okrservice.services.impl.UnitServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitServiceTests {
    @InjectMocks
    private UnitServiceImpl unitService;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private BusinessUnitService businessUnitService;

    @Test
    public void UnitService_CreateUnit_ReturnUnitDto() throws BusinessUnitNotFoundException, UnitAlreadyExistsException {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(new BusinessUnitId(1L, 1L))
                .build();

        Unit unit = Unit.builder()
                .businessUnit(businessUnit)
                .name("test unit")
                .build();
        UnitDto unitDto = unit.toDto();

        when(businessUnitService.findBusinessUnit(1L, 1L)).thenReturn(businessUnit);

        UnitDto createdUnit = unitService.createUnit(1L,1L, unitDto);

        Assertions.assertThat(createdUnit).isEqualTo(unitDto);
    }

    @Test
    public void UnitService_FindAllUnits_ReturnUnitDtoList() {
        ArrayList<Unit> units = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            Unit unit = Unit.builder()
                    .id(new UnitId(i, new BusinessUnitId(1L, 1L)))
                    .businessUnit(new BusinessUnit())
                    .name("test unit")
                    .build();
            units.add(unit);
        }

        ArrayList<UnitDto> unitDtos = units.stream()
                .map(Unit::toDto).collect(Collectors.toCollection(ArrayList::new));

        when(unitRepository.findByBusinessUnitId(new BusinessUnitId(1L,1L))).thenReturn(units);

        List<UnitDto> foundUnitDtos = unitService.findAllUnits(1L, 1L);

        Assertions.assertThat(foundUnitDtos).isEqualTo(unitDtos);
    }

    @Test
    public void UnitService_FindUnit_ReturnUnit() throws UnitNotFoundException {
        Unit unit = Unit.builder()
                .id(new UnitId(1L, new BusinessUnitId(1L, 1L)))
                .businessUnit(new BusinessUnit())
                .name("test unit")
                .build();

        when(unitRepository.findById(new UnitId(1L,new BusinessUnitId(1L,1L)))).thenReturn(Optional.of(unit));

        Unit foundUnit = unitService.findUnit(1L,1L,1L);

        Assertions.assertThat(foundUnit).isEqualTo(unit);
    }

    @Test
    public void UnitService_PatchUnit_ReturnUnitDto() throws UnitNotFoundException {
        Unit unit = Unit.builder()
                .id(new UnitId(1L, new BusinessUnitId(1L, 1L)))
                .businessUnit(new BusinessUnit())
                .name("test unit")
                .build();
        UnitDto unitDto = new UnitDto(unit);

        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));

        UnitDto patchedUnitDto = unitService.patchUnit(1L,1L,1L,unitDto);

        Assertions.assertThat(patchedUnitDto).isEqualTo(unitDto);
    }

    @Test
    public void UnitService_DeleteUnit_ReturnUnitDto() {
        assertAll(() -> unitService.deleteUnit(1L, 1L, 1L));
    }
}
