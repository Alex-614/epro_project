package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class ObjectiveDto {

    private Long id;
    
    @NotNull
    private Long deadline;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String description;
    
    @NotNull
    private Long ownerId;

    @PositiveOrZero
    public Integer achivement;

    public ObjectiveDto(Objective objective) {
        this.id = objective.getId();
        this.deadline = objective.getDeadline().toEpochMilli();
        this.title = objective.getTitle();
        this.description = objective.getDescription();
        this.ownerId = objective.getOwner().getId();
        this.achivement = objective.getAchivement();
    }
}
