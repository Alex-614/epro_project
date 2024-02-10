package de.thbingen.epro.project.okrservice.entities.buisinessunit;

import de.thbingen.epro.project.okrservice.entities.Objective;
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
@Table(name = "tbl_buisinessunitobjective")
@Entity
@PrimaryKeyJoinColumn(name = "objective_id")
public class BuisinessUnitObjective extends Objective {

    @ManyToOne
    @JoinColumn(name = "buisinessunit_id")
    private BuisinessUnit buisinessunit;


}
