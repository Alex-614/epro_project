package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuisinessUnitDto {

    private BuisinessUnitId id;
    @NotNull
    private String name;

}
