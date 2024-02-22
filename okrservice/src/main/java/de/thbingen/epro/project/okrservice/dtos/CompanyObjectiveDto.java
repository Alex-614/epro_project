package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CompanyObjectiveDto extends ObjectiveDto {
    public CompanyObjectiveDto(CompanyObjective companyObjective) {
        super(companyObjective);
    }
}
