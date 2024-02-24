package de.thbingen.epro.project.okrservice.entities.keyresults;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.ids.KeyResultUpdateId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_keyresultupdate")
@Entity
public class KeyResultUpdate {


    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "keyResultId", column = @Column(name = "keyresult_id")),
        @AttributeOverride(name = "oldKeyResultId", column = @Column(name = "old_keyresult_id"))
    })
    private KeyResultUpdateId id = new KeyResultUpdateId();

    public KeyResultUpdate(KeyResultUpdateId id) {
        this.id = id;
    }
    public KeyResultUpdate(String statusUpdate, Instant updateTimestamp, KeyResult keyResult, KeyResult oldKeyResult, KeyResult newKeyResult, User updater) {
        this.statusUpdate = statusUpdate;
        this.updateTimestamp = updateTimestamp;
        this.keyResult = keyResult;
        this.oldKeyResult = oldKeyResult;
        this.newKeyResult = newKeyResult;
        this.updater = updater;
    }

    @Column(name = "statusupdate")
    private String statusUpdate;
    
    @Column(name = "updatetimestamp")
    @CreationTimestamp
    private Instant updateTimestamp;

    @OneToOne
    @JoinColumn(name = "keyresult_id", referencedColumnName = "id")
    @MapsId("keyResultId")
    private KeyResult keyResult;

    @OneToOne
    @JoinColumn(name = "old_keyresult_id", referencedColumnName = "id")
    @MapsId("oldKeyResultId")
    private KeyResult oldKeyResult;
    
    @OneToOne
    @JoinColumn(name = "new_keyresult_id", referencedColumnName = "id")
    private KeyResult newKeyResult;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User updater;
    



}
