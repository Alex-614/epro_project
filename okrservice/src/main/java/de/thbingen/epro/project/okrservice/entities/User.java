package de.thbingen.epro.project.okrservice.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user")
@Entity
public class User {

    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;

    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;
    
    @Column(name = "firstname")
    private String firstname;
    
    @Column(name = "surname")
    private String surname;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_roleassignment")
    @MapKeyJoinColumn(name = "company_id")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Map<Company, Role> role = new HashMap<Company, Role>();
    public Map<Company, Role> getRoleAssignments() {
        return this.role;
    }
    public void setRoleAssignments(Map<Company, Role> roleAssignments) {
        this.role = roleAssignments;
    }
    public void addRoleAssignments(Map<Company, Role> roleAssignments) {
        this.role.putAll(roleAssignments);
    }



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tbl_company_employs_user",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "company_id", referencedColumnName = "id"
        )
    )
    private Set<Company> companys;
    public void addCompany(Company company) {
        this.companys.add(company);
    }


}
