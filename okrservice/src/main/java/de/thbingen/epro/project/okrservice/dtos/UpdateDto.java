package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateDto {
    @NotBlank
    private String statusUpdate;

    private Long updateTimestamp;

    private Long updaterId;

}
