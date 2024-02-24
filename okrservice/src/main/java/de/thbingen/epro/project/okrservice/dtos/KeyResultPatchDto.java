package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyResultPatchDto<T extends KeyResultDto> extends UpdateDto {


    public KeyResultPatchDto(String statusUpdate, Long updateTimestamp, Long updaterId, T keyResultDto) {
        super(statusUpdate, updateTimestamp, updaterId);
        this.keyResultDto = keyResultDto;
    }

    @NotNull
    private T keyResultDto;

}
