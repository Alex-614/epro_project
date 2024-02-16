package de.thbingen.epro.project.okrservice.entities.keyresults;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_keyresult")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class KeyResult {

    @Id
    @SequenceGenerator(name = "keyresult_id_seq", sequenceName = "keyresult_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keyresult_id_seq")
    private Long id;

    private int goal;

    private String title;

    private String description;

    private int current;

    private int confidenceLevel;

    @ManyToOne
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private KeyResultType type;
    

    @OneToOne(mappedBy = "newKeyResult")
    private KeyResultUpdate lastUpdate;

    // TODO change UpdateHistory pattern
    // should be unidirectional



}