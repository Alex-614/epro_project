package de.thbingen.epro.project.okrservice.entities.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KeyResultUpdateId {


    private Long userId;
    private Long oldKeyResultId;
    private Long newKeyResultId;




}
