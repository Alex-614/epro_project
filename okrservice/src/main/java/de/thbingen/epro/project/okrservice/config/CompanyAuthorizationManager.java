package de.thbingen.epro.project.okrservice.config;

import java.util.function.Supplier;

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

    private AuthorityString authorityString;
    private boolean shouldOwnObjective;

    public CompanyAuthorizationManager(AuthorityString authorityString, boolean shouldOwnObjective) {
        this.authorityString = authorityString;
        this.shouldOwnObjective = shouldOwnObjective;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        
        if (authorityString.isRole()) {
            authorityString.setAuthority(getRoleIdByName(authorityString.getAuthority()).toString());
        } else {
            authorityString.setAuthority(getPrivilegeIdByName(authorityString.getAuthority()).toString());
        }

        String temp; //required
        temp = context.getVariables().get("companyId");
        String companyId = temp != null ? temp : "";
        temp = context.getVariables().get("buisinessUnitId");
        String buisinessUnitId = temp != null ? temp : "";
        
        if (authorityString.getCompanyId().equals("{companyId}")) {
            authorityString.setCompanyId(companyId);
        }
        if (authorityString.getBuisinessUnitId().equals("{buisinessUnitId}")) {
            authorityString.setBuisinessUnitId(buisinessUnitId);
        }

        if (this.shouldOwnObjective) {
            temp = context.getVariables().get("objectiveId");
            String objectiveId = temp != null ? temp : "";

            if (!user.getOwnedObjectives().stream().filter(obj -> obj.getId().toString() == objectiveId).findFirst().isPresent()) {
                return new AuthorizationDecision(false);
            }
        }
        
        AuthorizationManager<RequestAuthorizationContext> delegate = AuthorityAuthorizationManager.hasAuthority(authorityString.toString());
        return delegate.check(authentication, context);
    }

    private Long getRoleIdByName(String name) {
        return roleRepository.existsByName(name) ? roleRepository.findByName(name).getId() : -1;
    }
    private Long getPrivilegeIdByName(String name) {
        return privilegeRepository.existsByName(name) ? privilegeRepository.findByName(name).getId() : -1;
    }


}
