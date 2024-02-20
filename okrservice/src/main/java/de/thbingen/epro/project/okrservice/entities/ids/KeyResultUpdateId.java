package de.thbingen.epro.project.okrservice.entities.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KeyResultUpdateId implements Serializable {


    @Column(name = "user_id")
    private Long userId;

    @Column(name = "old_keyresult_id")
    private Long oldKeyResultId;

    @Column(name = "new_keyresult_id")
    private Long newKeyResultId;




}
