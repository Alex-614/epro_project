package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyResultPatchDto<T extends KeyResultDto> {

    @NotEmpty(message = "'updateStatus' cannot be empty")
    private String statusUpdate;

    private Long updateTimestamp;

    @NotNull
    private T keyResultDto;

}
