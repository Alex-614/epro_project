package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Table(name = "tbl_companykeyresult")
@Entity
@PrimaryKeyJoinColumn(name = "keyresult_id")
public class CompanyKeyResult extends KeyResult {
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_companykeyresult_represents_businessunitobjective",
        joinColumns = @JoinColumn(
            name = "companykeyresult_id", referencedColumnName = "keyresult_id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "businessunitobjective_id", referencedColumnName = "objective_id"
        )
    )
    private List<BusinessUnitObjective> representers;



    @Override
    public int getAchivement() {
        int result = super.getAchivement();
        if (getRepresenters() != null) {
            for (BusinessUnitObjective o : getRepresenters()) {
                result += o.getAchivement();
            }
            if (getRepresenters().size() > 0) {
                result /= getRepresenters().size();
            }
        }
        return result;
    }

    public CompanyKeyResultDto toDto() {
        return new CompanyKeyResultDto(this);
    }

}
