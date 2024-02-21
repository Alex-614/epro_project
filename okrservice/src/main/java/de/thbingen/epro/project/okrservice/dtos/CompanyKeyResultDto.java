package de.thbingen.epro.project.okrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CompanyKeyResultDto extends KeyResultDto {
    private List<BusinessUnitObjective> representers;

    public CompanyKeyResultDto(CompanyKeyResult companyKeyResult) {
        super(companyKeyResult);
        this.representers = companyKeyResult.getRepresenters();
    }
}
