package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BusinessUnitObjectiveDto extends ObjectiveDto {

    private List<Long> represents;

    public BusinessUnitObjectiveDto(BusinessUnitObjective businessUnitObjective) {
        super(businessUnitObjective);
        this.represents = businessUnitObjective.getRepresented().stream().map(m -> m.getId()).collect(Collectors.toList());
    }

}