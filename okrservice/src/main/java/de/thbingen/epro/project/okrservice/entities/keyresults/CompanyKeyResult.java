package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.util.ArrayList;
import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "represented")
/*   @JoinTable(
        name = "tbl_businessunitobjective_represents_companykeyresult",
        joinColumns = @JoinColumn(
            name = "companykeyresult_id", referencedColumnName = "keyresult_id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "businessunitobjective_id", referencedColumnName = "objective_id"
        )
    ) */
    private List<BusinessUnitObjective> representers = new ArrayList<>();



    @Override
    public float getAchievement() {
        float result = super.getAchievement();
        if (getRepresenters() != null && getRepresenters().size() > 0) {
            for (BusinessUnitObjective o : getRepresenters()) {
                result += o.getAchievement();
            }
            result /= (getRepresenters().size() + 1);
        }
        return result;
    }

    @Override
    public CompanyKeyResultDto toDto() {
        return new CompanyKeyResultDto(this);
    }

}
