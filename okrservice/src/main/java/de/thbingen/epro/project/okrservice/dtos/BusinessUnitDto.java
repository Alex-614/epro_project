package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class BusinessUnitDto {

    private BusinessUnitId id;
    @NotEmpty(message = "'name' cannot be empty")
    private String name;

    public BusinessUnitDto(BusinessUnit businessUnit) {
        this.id = businessUnit.getId();
        this.name = businessUnit.getName();
    }

}
