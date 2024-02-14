package de.thbingen.epro.project.okrservice.entities.keyresults;

import de.thbingen.epro.project.okrservice.entities.Company;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "tbl_companykeyresult")
@Entity
@PrimaryKeyJoinColumn(name = "keyresult_id")
public class CompanyKeyResult extends KeyResult {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}