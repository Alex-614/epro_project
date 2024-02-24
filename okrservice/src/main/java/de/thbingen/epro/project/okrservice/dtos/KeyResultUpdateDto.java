package de.thbingen.epro.project.okrservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyResultUpdateDto<T extends KeyResultDto> extends UpdateDto {

    public KeyResultUpdateDto(String statusUpdate, Long updateTimestamp, Long updaterId) {
        super(statusUpdate, updateTimestamp, updaterId);
    }

    public KeyResultUpdateDto(String statusUpdate, Long updateTimestamp, Long updaterId, T newKeyResult, T oldKeyResult, T keyResult) {
        super(statusUpdate, updateTimestamp, updaterId);
        this.newKeyResult = newKeyResult;
        this.oldKeyResult = oldKeyResult;
        this.keyResult = keyResult;
    }

    private T newKeyResult;

    private T oldKeyResult;

    private T keyResult;



}
