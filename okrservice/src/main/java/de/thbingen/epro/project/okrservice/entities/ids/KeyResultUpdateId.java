package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KeyResultUpdateId implements Serializable {

    @Column(name = "keyresult_id")
    private Long keyResultId;
    
    @Column(name = "old_keyresult_id")
    private Long oldKeyResultId;




}
