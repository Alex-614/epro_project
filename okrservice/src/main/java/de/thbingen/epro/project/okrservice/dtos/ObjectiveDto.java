package de.thbingen.epro.project.okrservice.dtos;

import java.time.Instant;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ObjectiveDto {

    private Long id;
    
    @NotNull(message = "'deadline' cannot be null")
    private Instant deadline;
    
    @NotNull(message = "'title' cannot be null")
    private String title;
    
    @NotNull(message = "'description' cannot be null")
    private String description;
    
    @NotNull(message = "'ownerId' cannot be null")
    private Long ownerId;

    public ObjectiveDto(Objective objective) {
        this.id = objective.getId();
        this.deadline = objective.getDeadline();
        this.title = objective.getTitle();
        this.description = objective.getDescription();
        this.ownerId = objective.getOwner().getId();
    }
}
