package de.thbingen.epro.project.okrservice.dtos;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
    


}
