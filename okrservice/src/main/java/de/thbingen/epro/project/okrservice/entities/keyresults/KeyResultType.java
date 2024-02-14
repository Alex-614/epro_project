package de.thbingen.epro.project.okrservice.entities.keyresults;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_keyresulttype")
@Entity
public class KeyResultType {

    @Id
    private String name;

}
