package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
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
        this.representers = companyKeyResult.getRepresenters().stream().map(m -> m.getId().longValue()).collect(Collectors.toList());
    }
    



}
