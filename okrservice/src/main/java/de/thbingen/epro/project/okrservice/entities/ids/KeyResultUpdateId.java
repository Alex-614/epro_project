package de.thbingen.epro.project.okrservice.entities.ids;

import jakarta.persistence.Column;
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

    @Column(name = "old_keyresult_id")
    private Long oldKeyResultId;

    @Column(name = "new_keyresult_id")
    private Long newKeyResultId;




}
