package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UnitDto {

    private UnitId id;
    @NotEmpty(message = "'name' cannot be empty")
    private String name;


}
