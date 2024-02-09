package de.thbingen.epro.project.okrservice.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.Privilege;
import de.thbingen.epro.project.okrservice.entities.Role;
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

    private Collection<GrantedAuthority> mapRoleAssignmentsToAuthorities(Map<Company, Role> roleAssignments) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Map.Entry<Company, Role> assignment : roleAssignments.entrySet()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + assignment.getValue().getName() + "_" + assignment.getKey().getId()));
            for (Privilege privilege : assignment.getValue().getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(privilege.getName() + "_" + assignment.getKey().getId()));
            }
        }
        return authorities;
    }



}
