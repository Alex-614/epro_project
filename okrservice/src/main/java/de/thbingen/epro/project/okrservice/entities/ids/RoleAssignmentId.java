package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RoleAssignmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "company_id")
    private Long companyId;

}
