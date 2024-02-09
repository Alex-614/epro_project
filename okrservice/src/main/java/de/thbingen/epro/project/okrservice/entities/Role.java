package de.thbingen.epro.project.okrservice.entities;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "tbl_role_includes_privileges",
        joinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "privilege_id", referencedColumnName = "id"
        )
    )
    private Collection<Privilege> privileges;

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
    private Collection<Role> inheritedRoles;


}
