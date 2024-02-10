package de.thbingen.epro.project.okrservice.entities;

import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<RoleAssignment> roleAssignments;
    public void addAllRoleAssignment(List<RoleAssignment> roleAssignments) {
        this.roleAssignments.addAll(roleAssignments);
    }
    public void addRoleAssignment(RoleAssignment roleAssignment) {
        roleAssignments.add(roleAssignment);
    }
    public void removeRoleAssignment(RoleAssignment roleAssignment) {
        roleAssignments.remove(roleAssignment);
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
    public void removeCompany(Company company) {
        this.companys.remove(company);
    }


}
