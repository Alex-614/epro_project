package de.thbingen.epro.project.okrservice.config;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import de.thbingen.epro.project.okrservice.AuthorityString;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.PrivilegeRepository;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CompanyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PrivilegeRepository privilegeRepository;

    private CompanyAuthority[] companyAuthorities;

    public CompanyAuthorizationManager(CompanyAuthority[] companyAuthorities) {
        this.companyAuthorities = companyAuthorities;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        
        //String[] authorities = new String[companyAuthorities.length];

        AuthorizationDecision decision = new AuthorizationDecision(false);
        int i = 0;
        while (!decision.isGranted() && i < companyAuthorities.length) {

            CompanyAuthority companyAuthority = companyAuthorities[i];
            AuthorityString authorityString = companyAuthority.getAuthorityString();
            boolean shouldOwnObjective = companyAuthority.isShouldOwnObjective();
            List<String> methods = Arrays.stream(companyAuthority.getMethods()).map(mapper -> mapper.name()).collect(Collectors.toList());
            
            if (methods.contains(context.getRequest().getMethod())) {    
                // translate Role/Privilege name to Id
                if (authorityString.isRole()) {
                    authorityString.setAuthority(getRoleIdByName(authorityString.getAuthority()).toString());
                } else {
                    authorityString.setAuthority(getPrivilegeIdByName(authorityString.getAuthority()).toString());
                }
        
                // set variables from URI
                if (authorityString.getCompanyId().equals("{companyId}")) {
                    String companyId = context.getVariables().get("companyId"); // get URI variable
                    if (companyId == null) companyId = "";
                    authorityString.setCompanyId(companyId);
                }
                if (authorityString.getBuisinessUnitId().equals("{buisinessUnitId}")) {
                    String buisinessUnitId = context.getVariables().get("buisinessUnitId"); // get URI variable
                    if (buisinessUnitId == null) buisinessUnitId = "";
                    authorityString.setBuisinessUnitId(buisinessUnitId);
                }
                
                if (shouldOwnObjective) {
                    // must be final to stream
                    final String objectiveId = context.getVariables().get("objectiveId") != null ? context.getVariables().get("objectiveId") : "";
    
                    // if userOwnedObjectives contains objectiveId
                    if (!user.getOwnedObjectives().stream().filter(obj -> obj.getId().toString() == objectiveId).findFirst().isPresent()) {
                        return new AuthorizationDecision(false);
                    }
                }
                
                decision = AuthorityAuthorizationManager.hasAuthority(authorityString.toString()).check(authentication, context);
            }

            i++;
        }

/* 
        for (int i = 0; i < companyAuthorities.length; i++) {
            CompanyAuthority companyAuthority = companyAuthorities[i];
            AuthorityString authorityString = companyAuthority.getAuthorityString();
            boolean shouldOwnObjective = companyAuthority.isShouldOwnObjective();

            // translate Role/Privilege name to Id
            if (authorityString.isRole()) {
                authorityString.setAuthority(getRoleIdByName(authorityString.getAuthority()).toString());
            } else {
                authorityString.setAuthority(getPrivilegeIdByName(authorityString.getAuthority()).toString());
            }
    
            // set variables from URI
            if (authorityString.getCompanyId().equals("{companyId}")) {
                String companyId = context.getVariables().get("companyId"); // get URI variable
                if (companyId == null) companyId = "";
                authorityString.setCompanyId(companyId);
            }
            if (authorityString.getBuisinessUnitId().equals("{buisinessUnitId}")) {
                String buisinessUnitId = context.getVariables().get("buisinessUnitId"); // get URI variable
                if (buisinessUnitId == null) buisinessUnitId = "";
                authorityString.setBuisinessUnitId(buisinessUnitId);
            }
    
            if (shouldOwnObjective) {
                // must be final to stream
                final String objectiveId = context.getVariables().get("objectiveId") != null ? context.getVariables().get("objectiveId") : "";

                // if userOwnedObjectives contains objectiveId
                if (!user.getOwnedObjectives().stream().filter(obj -> obj.getId().toString() == objectiveId).findFirst().isPresent()) {
                    return new AuthorizationDecision(false);
                }
            }

            authorities[i] = authorityString.toString();
        }
        AuthorizationManager<RequestAuthorizationContext> delegate = AuthorityAuthorizationManager.hasAnyAuthority(authorities);
        return delegate.check(authentication, context);
*/
        return decision;
    }

    private Long getRoleIdByName(String name) {
        return roleRepository.existsByName(name) ? roleRepository.findByName(name).getId() : -1;
    }
    private Long getPrivilegeIdByName(String name) {
        return privilegeRepository.existsByName(name) ? privilegeRepository.findByName(name).getId() : -1;
    }


}
