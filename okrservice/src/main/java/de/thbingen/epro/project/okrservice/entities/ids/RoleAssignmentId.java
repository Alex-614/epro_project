package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class RoleAssignmentId implements Serializable {

    @Column(name = "user_id")
    @NotNull
    private Long userId;
    
    @Column(name = "role_id")
    @NotNull
    private Long roleId;
    
    @Column(name = "company_id")
    @NotNull
    private Long companyId;

}
