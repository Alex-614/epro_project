package de.thbingen.epro.project.okrservice.entities;

import de.thbingen.epro.project.okrservice.entities.ids.KeyResultUpdateId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_keyresultupdate")
@Entity
public class KeyResultUpdate {


    @EmbeddedId
    private KeyResultUpdateId id;








    
}
