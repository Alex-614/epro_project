package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.keyresults.BusinessUnitKeyResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessUnitKeyResultDto extends KeyResultDto {
    public BusinessUnitKeyResultDto(BusinessUnitKeyResult businessUnitKeyResult) {
        super(businessUnitKeyResult);
    }
}
