package de.thbingen.epro.project.okrservice.dtos;

import java.time.Instant;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ObjectiveDto {

    private Long id;
    
    @NotNull
    private Instant deadline;
    
    @NotEmpty
    private String title;
    
    @NotEmpty
    private String description;
    
    @NotNull
    private Long ownerId;

    public ObjectiveDto(Objective objective) {
        this.id = objective.getId();
        this.deadline = objective.getDeadline();
        this.title = objective.getTitle();
        this.description = objective.getDescription();
        this.ownerId = objective.getOwner().getId();
    }
}
