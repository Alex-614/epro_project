package de.thbingen.epro.project.okrservice.dtos;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ObjectiveDto {

    private Long id;

    @NotNull
    private Instant deadline;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Long ownerId;

}
