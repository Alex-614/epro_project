package de.thbingen.epro.project.okrservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyResultUpdateDto<T extends KeyResultDto> {

    private KeyResultPatchDto<T> newKeyResult;

    private T oldKeyResult;

    private T keyResult;






}
