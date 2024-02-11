package de.thbingen.epro.project.okrservice.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.AuthorityString;
import de.thbingen.epro.project.okrservice.entities.Privilege;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("email not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRoleAssignmentsToAuthorities(user.getRoleAssignments()));
    }

    private Collection<GrantedAuthority> mapRoleAssignmentsToAuthorities(List<RoleAssignment> roleAssignments) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleAssignment assignment : roleAssignments) {
            String companyId = assignment.getCompany().getId().toString();
            authorities.addAll(mapRoleToAuthorities(assignment.getRole(), companyId));
        }
        return authorities;
    }
    
    private List<GrantedAuthority> mapRoleToAuthorities(Role role, String companyId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityString.Role(role.getId().toString(), companyId).toString()));
        for (Role inheritedRole : role.getInheritedRoles()) {
            authorities.addAll(mapRoleToAuthorities(inheritedRole, companyId));
        }
        for (Privilege privilege : role.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(AuthorityString.Privilege(privilege.getId().toString(), companyId).toString()));
        }
        return authorities;
    }



}
