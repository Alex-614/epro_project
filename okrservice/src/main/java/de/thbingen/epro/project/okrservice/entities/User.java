package de.thbingen.epro.project.okrservice.entities;

import java.util.ArrayList;
import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.objectives.BusinessUnitObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.entities.objectives.Objective;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private List<RoleAssignment> roleAssignments = new ArrayList<>();

    
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Objective> ownedObjectives = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<CompanyObjective> ownedCompanyObjectives = new ArrayList<>();
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<BusinessUnitObjective> ownedBusinessUnitObjectives = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_company_employs_user",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "company_id", referencedColumnName = "id"
        )
    )
    private List<Company> companies = new ArrayList<>();
    public void addCompany(Company company) {
        this.companies.add(company);
    }
    public void removeCompany(Company company) {
        this.companies.remove(company);
    }




    public UserDto toDto() {
        return new UserDto(this);
    }








}
