package de.thbingen.epro.project.okrservice.entities;

import org.hibernate.annotations.GenericGenerator;

import de.thbingen.epro.project.okrservice.entities.buisinessunit.BuisinessUnit;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_unit")
@Entity
public class Unit {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_id_gen")
    @GenericGenerator(
        name = "unit_id_gen", 
        strategy = "de.thbingen.epro.project.okrservice.entities.ids.UnitIdGenerator")
    private UnitId id;

    @MapsId("buisinessUnitId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns( {
        @JoinColumn(name = "buisinessunit_id", referencedColumnName = "id"), 
        @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    })
    private BuisinessUnit buisinessUnit;
    


    
    @Column(name = "name")
    private String name;

    


}
