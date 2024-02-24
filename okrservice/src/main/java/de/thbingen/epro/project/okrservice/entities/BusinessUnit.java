package de.thbingen.epro.project.okrservice.entities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import de.thbingen.epro.project.okrservice.dtos.BusinessUnitDto;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
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
@Table(name = "tbl_businessunit")
@Entity
public class BusinessUnit {



    public BusinessUnit(BusinessUnitId id) {
        this.id = id;
    }



    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "businessunit_id_gen")
    @GenericGenerator(
        name = "businessunit_id_gen", 
        strategy = "de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitIdGenerator")
    private BusinessUnitId id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("companyId")
    private Company company;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "businessUnit")
    private List<BusinessUnitObjective> objectives;


    public BusinessUnitDto toDto() {
        return new BusinessUnitDto(this);
    }



}
