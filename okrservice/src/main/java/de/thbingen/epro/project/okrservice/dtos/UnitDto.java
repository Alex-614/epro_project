package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnitDto {

    private UnitId id;
    @NotNull
    private String name;


}
