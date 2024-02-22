package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;

import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyKeyResultDto extends KeyResultDto {
    private List<BusinessUnitObjective> representers;

    public CompanyKeyResultDto(CompanyKeyResult companyKeyResult) {
        super(companyKeyResult);
        this.representers = companyKeyResult.getRepresenters();
    }
}
