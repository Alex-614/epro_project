package de.thbingen.epro.project.okrservice.entities;

import de.thbingen.epro.project.okrservice.entities.ids.RoleAssignmentId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_roleassignment")
@Entity
public class RoleAssignment {

    @EmbeddedId
    private RoleAssignmentId id = new RoleAssignmentId();
    
    public RoleAssignment(User user, Role role, Company company) {
        this.user = user;
        this.role = role;
        this.company = company;
    }

    @ManyToOne
    @MapsId("userId")
    private User user;
    
    @ManyToOne
    @MapsId("roleId")
    private Role role;
    
    @ManyToOne
    @MapsId("companyId")
    private Company company;


    


}
