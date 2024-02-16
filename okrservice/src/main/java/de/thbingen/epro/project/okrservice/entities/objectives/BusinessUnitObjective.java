package de.thbingen.epro.project.okrservice.entities.objectives;

import java.util.List;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tbl_businessunitobjective")
@Entity
@PrimaryKeyJoinColumn(name = "objective_id")
public class BusinessUnitObjective extends Objective {


    @ManyToOne
    @JoinColumns( { 
        @JoinColumn(name = "businessunit_id", referencedColumnName = "id"),
        @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    })
    private BusinessUnit businessUnit;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_companykeyresult_represents_businessunitobjective",
        joinColumns = @JoinColumn(
            name = "businessunitobjective_id", referencedColumnName = "objective_id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "companykeyresult_id", referencedColumnName = "keyresult_id"
        )
    )
    private List<CompanyKeyResult> represented;


}
