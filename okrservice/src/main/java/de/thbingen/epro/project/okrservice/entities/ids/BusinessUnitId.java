package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Getter
public class BusinessUnitId implements Serializable {

    private Long id;
    
    @Column(name = "company_id")
    private Long companyId;



}
