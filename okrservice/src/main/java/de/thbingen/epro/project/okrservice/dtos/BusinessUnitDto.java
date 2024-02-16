package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BusinessUnitDto {

    private BusinessUnitId id;
    @NotEmpty(message = "'name' cannot be empty")
    private String name;

}
