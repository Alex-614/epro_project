package de.thbingen.epro.project.okrservice.entities.ids;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UpdateHistoryId {


    private Long userId;
    private Long oldKeyResultId;
    private Long newKeyResultId;




}
