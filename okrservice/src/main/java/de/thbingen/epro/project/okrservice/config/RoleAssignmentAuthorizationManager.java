package de.thbingen.epro.project.okrservice.config;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

public class RoleAssignmentAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    
    private Long roleId;

    public RoleAssignmentAuthorizationManager(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        AuthorizationManager<RequestAuthorizationContext> delegate = AuthorityAuthorizationManager.hasAuthority(createAuthority(context));
        return delegate.check(authentication, context);
    }


    private String createAuthority(RequestAuthorizationContext context) {
        String companyId = context.getVariables().get("companyId");
        return String.format("%s%d_%s", SecurityConstants.ROLE_PREFIX, this.roleId, companyId);
    }


    

}
