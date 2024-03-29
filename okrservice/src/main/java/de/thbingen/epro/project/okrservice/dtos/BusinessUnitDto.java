package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessUnitDto {

    private BusinessUnitId id;
    
    @NotBlank
    private String name;

    public BusinessUnitDto(BusinessUnit businessUnit) {
        this.id = businessUnit.getId();
        this.name = businessUnit.getName();
    }

}
