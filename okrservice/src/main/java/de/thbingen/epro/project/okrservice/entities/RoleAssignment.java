package de.thbingen.epro.project.okrservice.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_roleassignment")
@Entity
public class RoleAssignment {

    @EmbeddedId
    private RoleAssignmentId id;
    
    @ManyToOne
    @MapsId("userId")
    private User user;
    
    @ManyToOne
    @MapsId("roleId")
    private Role role;
    
    @ManyToOne
    @MapsId("companyId")
    private Company company;

/* 
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
    
    @ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;
 */


    @Data
    @Embeddable
    public static class RoleAssignmentId implements Serializable {

        @Column(name = "user_id")
        private Long userId;
        @Column(name = "role_id")
        private Long roleId;
        @Column(name = "company_id")
        private Long companyId;

    }


}
