package de.thbingen.epro.project.okrservice.entities.objectives;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.thbingen.epro.project.okrservice.dtos.ObjectiveDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "tbl_objective")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Objective {

    @Id
    @SequenceGenerator(name = "objective_id_seq", sequenceName = "objective_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objective_id_seq")
    private Long id;

    private Instant deadline;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    
    @OneToMany(mappedBy = "objective")
    private Set<KeyResult> keyReslts = new HashSet<>();








    public float getAchievement() {
        float result = 0;
        if (getKeyReslts() != null) {
            for (KeyResult k : getKeyReslts()) {
                result += k.getAchievement();
            }
            // if there is at least one KeyResult
            if (result != 0) {
                result /= getKeyReslts().size();
            }
        }
        return result;
    }

    public abstract <T extends ObjectiveDto> T toDto();

}
