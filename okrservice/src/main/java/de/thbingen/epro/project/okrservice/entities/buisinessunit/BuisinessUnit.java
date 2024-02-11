package de.thbingen.epro.project.okrservice.entities.buisinessunit;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import de.thbingen.epro.project.okrservice.entities.company.Company;
import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_buisinessunit")
@Entity
public class BuisinessUnit {




    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buisinessunit_id_gen")
    @GenericGenerator(
        name = "buisinessunit_id_gen", 
        strategy = "de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitIdGenerator")
    private BuisinessUnitId id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("companyId")
    private Company company;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "buisinessunit")
    private List<BuisinessUnitObjective> objectives;






}
