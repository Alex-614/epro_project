package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {
    @NotBlank
    private String statusUpdate;

    private Long updateTimestamp;

    @PositiveOrZero
    private Long updaterId;

}
