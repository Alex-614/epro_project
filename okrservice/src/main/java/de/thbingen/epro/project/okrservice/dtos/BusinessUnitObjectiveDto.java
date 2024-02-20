package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessUnitObjectiveDto extends ObjectiveDto {
    public BusinessUnitObjectiveDto(BusinessUnitObjective businessUnitObjective){
        super(businessUnitObjective);
    }
}