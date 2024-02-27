package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.thbingen.epro.project.okrservice.constants.GlobalConstants;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.entities.BusinessUnit;
import de.thbingen.epro.project.okrservice.entities.Unit;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "tbl_keyresult")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class KeyResult {

    @Id
    @SequenceGenerator(name = "keyresult_id_seq", sequenceName = "keyresult_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keyresult_id_seq")
    private Long id;

    public KeyResult(Long id) {
        this.id = id;
    }

    private int goal;

    private String title;

    private String description;

    private int current;

    @Column(name = "confidencelevel")
    private int confidenceLevel;

    @ManyToOne
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @ManyToOne
    @JoinColumn(name = "type_name")
    private KeyResultType type;
    
    @OneToOne(mappedBy = "newKeyResult", optional = true)
    private KeyResultUpdate lastUpdate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_unit_contributes_keyresult",
        joinColumns = @JoinColumn(
            name = "keyresult_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = {@JoinColumn(name = "unit_id", referencedColumnName = "id"), 
                              @JoinColumn(name = "businessunit_id", referencedColumnName = "businessunit_id"), 
                              @JoinColumn(name = "company_id", referencedColumnName = "company_id")}
    )
    private Set<Unit> contributingUnits = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_businessunit_contributes_keyresult",
        joinColumns = @JoinColumn(
            name = "keyresult_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = {@JoinColumn(name = "businessunit_id", referencedColumnName = "id"), 
                              @JoinColumn(name = "company_id", referencedColumnName = "company_id")}
    )
    private Set<BusinessUnit> contributingBusinessUnits = new HashSet<>();



    public float getAchievement() {
        if (getGoal() == 0) {
            return 0;
        }
        return Float.parseFloat(new DecimalFormat(GlobalConstants.ACHIEVEMENT_PATTERN).format((float) getCurrent() / (float) getGoal() * 100));
    }


    public abstract <T extends KeyResultDto> T toDto();

    

}