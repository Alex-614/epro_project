package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BusinessUnitKeyResultDto extends KeyResultDto {
    public BusinessUnitKeyResultDto(BusinessUnitKeyResult businessUnitKeyResult) {
        super(businessUnitKeyResult);
    }
}
