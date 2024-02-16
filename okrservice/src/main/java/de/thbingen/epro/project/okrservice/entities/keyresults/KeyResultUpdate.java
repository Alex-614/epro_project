package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.time.Instant;

import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.KeyResultUpdateId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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


    private String statusUpdate;

    private Instant timestamp;


    @OneToOne
    @MapsId("oldKeyResultId")
    private KeyResult oldKeyResult;
    
    @OneToOne
    @MapsId("newKeyResultId")
    private KeyResult newKeyResult;
    
    @OneToOne
    @MapsId("userId")
    private User updater;

    

}
