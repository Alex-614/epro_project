package de.thbingen.epro.project.okrservice.entities.keyresults;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitKeyResultDto;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "tbl_businessunitkeyresult")
@Entity
@PrimaryKeyJoinColumn(name = "keyresult_id")
public class BusinessUnitKeyResult extends KeyResult {

    @Override
    public BusinessUnitKeyResultDto toDto() {
        return new BusinessUnitKeyResultDto(this);
    }

}
