package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BuisinessUnitDto {

    private BuisinessUnitId id;
    @NotEmpty(message = "'name' cannot be empty")
    private String name;

}
