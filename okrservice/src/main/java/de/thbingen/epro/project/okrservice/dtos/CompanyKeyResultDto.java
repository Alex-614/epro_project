package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyKeyResultDto extends KeyResultDto {

    private List<Long> representers;

    public CompanyKeyResultDto(CompanyKeyResult companyKeyResult) {
        super(companyKeyResult);
        this.representers = companyKeyResult.getRepresenters().stream().map(mapper -> mapper.getId()).collect(Collectors.toList());
    }
    public CompanyKeyResultDto(KeyResult keyResult, List<BusinessUnitObjective> representers) {
        super(keyResult);
        this.representers = representers.stream().map(mapper -> mapper.getId().longValue()).collect(Collectors.toList());
    }
}
