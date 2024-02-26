package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleAssignmentRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.services.impl.CompanyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {
    @InjectMocks
    private CompanyServiceImpl companyService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserService userService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleAssignmentRepository roleAssignmentRepository;

    @Test
    public void CompanyService_CreateCompany_ReturnCompanyDto() throws Exception {
        Company company = Company.builder()
                .name("CompanyA")
                .objectives(new ArrayList<>())
                .build();
        CompanyDto companyDto = new CompanyDto(company);

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur")
                .companies(new ArrayList<>())
                .build();

        user.addCompany(company);

        Role role = new Role();

        when(companyRepository.save(company)).thenReturn(company);
        when(userService.findUser(user.getId())).thenReturn(user);
        when(roleRepository.findByName(Mockito.any(String.class))).thenReturn(role);
        when(roleAssignmentRepository.save(Mockito.any(RoleAssignment.class)))
                .thenReturn(new RoleAssignment(user, role, company));

        CompanyDto createdCompanyDto = companyService.createCompany(1L, companyDto);

        Assertions.assertThat(createdCompanyDto).isEqualTo(companyDto);
    }

    @Test
    public void CompanyService_FindCompany_ReturnCompany() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

        Company foundCompany = companyService.findCompany(company.getId());

        Assertions.assertThat(foundCompany).isEqualTo(company);
    }

    @Test
    public void CompanyService_FindAllCompanies_ReturnCompanyDtoList() {
        ArrayList<Company> companies = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            Company company = Company.builder()
                    .id(i)
                    .name("CompanyA").build();
            companies.add(company);
        }

        ArrayList<CompanyDto> companyDtos = companies.stream()
                .map(CompanyDto::new).collect(Collectors.toCollection(ArrayList::new));


        when(companyRepository.findAll()).thenReturn(companies);

        List<CompanyDto> foundCompanies = companyService.findAllCompanies();

        Assertions.assertThat(foundCompanies).isEqualTo(companyDtos);
    }

    @Test
    public void CompanyService_PatchCompany_ReturnCompanyDto() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        CompanyDto companyDto = company.toDto();

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

        CompanyDto patchedCompany = companyService.patchCompany(companyDto.getId(), companyDto);

        Assertions.assertThat(patchedCompany).isEqualTo(companyDto);
    }

    @Test
    public void CompanyService_DeleteCompany_ReturnVoid() {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        assertAll(() -> companyService.deleteCompany(company.getId()));
    }

    @Test
    public void CompanyService_AddUser_ReturnVoid() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur")
                .companies(new ArrayList<>())
                .build();

        user.addCompany(company);

        Role role = new Role();

        ArrayList<Number> roleIds = new ArrayList<>();
        roleIds.add(1);
        RolesDto rolesDto = new RolesDto();
        rolesDto.setRoleIds(roleIds);

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(userService.findUser(user.getId())).thenReturn(user);
        when(roleRepository.existsById(1L)).thenReturn(true);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        companyService.addUser(company.getId(), user.getId(), rolesDto);

        RoleAssignment roleAssignment = new RoleAssignment(user, role, company);

        verify(roleAssignmentRepository, times(1)).save(roleAssignment);
    }

    @Test
    public void CompanyService_DeleteUser_ReturnVoid() throws Exception {
        Company company = Company.builder()
                .id(1L)
                .name("CompanyA").build();

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur")
                .companies(new ArrayList<>())
                .build();

        user.addCompany(company);

        ArrayList<Number> roleIds = new ArrayList<>();
        roleIds.add(1);
        RolesDto rolesDto = new RolesDto();
        rolesDto.setRoleIds(roleIds);

        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(userService.findUser(user.getId())).thenReturn(user);

        companyService.removeUser(company.getId(), user.getId());

        verify(roleAssignmentRepository, times(1))
                .deleteByCompanyAndUserEquals(company, user);
    }
}
