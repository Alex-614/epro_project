package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnitDto {

    private UnitId id;
    
    @NotEmpty
    private String name;

    public UnitDto(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
    }
}
