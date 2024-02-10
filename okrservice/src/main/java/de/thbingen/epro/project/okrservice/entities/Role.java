package de.thbingen.epro.project.okrservice.entities;

import java.util.List;

import de.thbingen.epro.project.okrservice.config.SecurityConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_role")
@Entity
public class Role {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public String getFormalName() {
        return SecurityConstants.ROLE_PREFIX + getName();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_role_includes_privilege",
        joinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "privilege_id", referencedColumnName = "id"
        )
    )
    private List<Privilege> privileges;

    @ManyToMany
    @JoinTable(
        name = "tbl_role_inherits_role",
        joinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "inherit_id", referencedColumnName = "id"
        )
    )
    private List<Role> inheritedRoles;


}
