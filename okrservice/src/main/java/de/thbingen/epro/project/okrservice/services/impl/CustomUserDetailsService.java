package de.thbingen.epro.project.okrservice.services.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.entities.Privilege;
import de.thbingen.epro.project.okrservice.entities.Role;
import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.security.AuthorityString;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRoleAssignmentsToAuthorities(user.getRoleAssignments()));
    }

    private Collection<GrantedAuthority> mapRoleAssignmentsToAuthorities(List<RoleAssignment> roleAssignments) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleAssignment assignment : roleAssignments) {
            String companyId = assignment.getCompany().getId().toString();
            authorities.addAll(mapRoleToAuthoritiesRecursive(assignment.getRole(), companyId));
        }
        return authorities;
    }
    
    private Set<GrantedAuthority> mapRoleToAuthoritiesRecursive(Role role, String companyId) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityString.Role(role.getId().toString(), companyId).toString()));
        for (Role inheritedRole : role.getInheritedRoles()) {
            authorities.addAll(mapRoleToAuthoritiesRecursive(inheritedRole, companyId));
        }
        for (Privilege privilege : role.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(AuthorityString.Privilege(privilege.getId().toString(), companyId).toString()));
        }
        return authorities;
    }


}
