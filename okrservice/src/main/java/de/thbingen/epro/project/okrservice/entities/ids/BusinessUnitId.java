package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BusinessUnitId implements Serializable {

    @NotNull
    private Long id;
    
    @NotNull
    private Long companyId;



}
