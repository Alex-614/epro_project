package de.thbingen.epro.project.okrservice.entities.objectives;

import java.time.Instant;

import de.thbingen.epro.project.okrservice.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    






}
