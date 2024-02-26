package de.thbingen.epro.project.okrservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.constants.Roles;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.RolesDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.exceptions.CompanyNotFoundException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleAssignmentRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.services.CompanyService;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class CompanyServiceImpl implements CompanyService {


    private CompanyRepository companyRepository;

    private UserService userService;

    private RoleRepository roleRepository;

    private RoleAssignmentRepository roleAssignmentRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, UserService userService, 
                            RoleRepository roleRepository, RoleAssignmentRepository roleAssignmentRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
    }

    
    @Override
    public Company findCompany(long companyId) throws CompanyNotFoundException {
        return companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException());
    }


    @Override
    public CompanyDto createCompany(long userId, CompanyDto companyDto) throws UserNotFoundException {
        Company company = new Company();
        company.setName(companyDto.getName());
        companyRepository.save(company);
        
        User user = userService.findUser(userId);
        user.addCompany(company);

        Role role = roleRepository.findByName(Roles.CO_OKR_ADMIN.getName());
        roleAssignmentRepository.save(new RoleAssignment(user, role, company));

        //TODO remove before deployment
        role = roleRepository.findByName(Roles.BUO_OKR_ADMIN.getName());
        roleAssignmentRepository.save(new RoleAssignment(user, role, company));

        return company.toDto();
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(m -> m.toDto()).collect(Collectors.toList());
    }

    @Override
    public CompanyDto patchCompany(long companyId, CompanyDto companyDto) throws CompanyNotFoundException {
        Company company = findCompany(companyId);

        if (companyDto.getName() != null && !companyDto.getName().trim().isEmpty()) 
            company.setName(companyDto.getName());

        companyRepository.save(company);
        return company.toDto();
    }

    @Override
    public void deleteCompany(long companyId) {
        companyRepository.deleteById(companyId);
    }

    @Override
    public void addUser(long companyId, long userId, RolesDto roleDto) throws CompanyNotFoundException, UserNotFoundException {
        Company company = findCompany(companyId);
        User target = userService.findUser(userId);
        for (Number roleId : roleDto.getRoleIds()) {
            if (roleRepository.existsById(roleId.longValue())) {
                Role role = roleRepository.findById(roleId.longValue()).get();
                roleAssignmentRepository.save(new RoleAssignment(target, role, company));
            }
        }
    }

    @Override
    public void removeUser(long companyId, long userId) throws CompanyNotFoundException, UserNotFoundException {
        Company company = findCompany(companyId);
        User target = userService.findUser(userId);
        roleAssignmentRepository.deleteByCompanyAndUserEquals(company, target);
    }

}
