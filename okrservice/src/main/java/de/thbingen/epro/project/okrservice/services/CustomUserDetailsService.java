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

import de.thbingen.epro.project.okrservice.config.SecurityConstants;
import de.thbingen.epro.project.okrservice.entities.Privilege;
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
            authorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + assignment.getRole().getId() + "_" + companyId));
            for (Privilege privilege : assignment.getRole().getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(privilege.getId() + "_" + companyId));
            }
        }
        return authorities;
    }



}
