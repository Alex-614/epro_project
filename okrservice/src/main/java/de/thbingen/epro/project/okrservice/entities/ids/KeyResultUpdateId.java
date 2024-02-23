package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KeyResultUpdateId implements Serializable {

    @Column(name = "keyresult_id")
    @NotNull
    private Long keyResultId;
    
    @Column(name = "old_keyresult_id")
    @NotNull
    private Long oldKeyResultId;




}
