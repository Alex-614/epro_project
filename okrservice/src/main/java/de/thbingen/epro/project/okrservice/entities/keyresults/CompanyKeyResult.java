package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.util.List;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_companykeyresult")
@Entity
@PrimaryKeyJoinColumn(name = "keyresult_id")
public class CompanyKeyResult extends KeyResult {
    
    @ManyToMany(mappedBy = "represented", fetch = FetchType.EAGER)
    private List<BusinessUnitObjective> representers;


}
