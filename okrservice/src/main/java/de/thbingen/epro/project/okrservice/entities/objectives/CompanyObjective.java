package de.thbingen.epro.project.okrservice.entities.objectives;

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
@Table(name = "tbl_companyobjective")
@Entity
@PrimaryKeyJoinColumn(name = "objective_id")
public class CompanyObjective extends Objective {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;



}
