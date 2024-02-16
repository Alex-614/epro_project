package de.thbingen.epro.project.okrservice.entities.keyresults;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
@Table(name = "tbl_businessunitkeyresult")
@Entity
@PrimaryKeyJoinColumn(name = "keyresult_id")
public class BusinessUnitKeyResult extends KeyResult {

    
    @ManyToOne
    @JoinColumns( { 
        @JoinColumn(name = "businessunit_id", referencedColumnName = "id"),
        @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    })
    private BusinessUnit businessUnit;

}
