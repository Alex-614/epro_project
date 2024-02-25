package de.thbingen.epro.project.okrservice.constants;

import de.thbingen.epro.project.okrservice.security.SecurityConstants;

/**
 * This Java code defines an enum called `Roles` which represents different roles in the system. 
 * Each role has a corresponding name and formal name.
 * <hr />
 * <h3>This class does not validate or communicate with the database.</h4>
 * <h4>It is a static final provision of `Roles`</h3>
 * <hr />
 * @param name the custom name of the Role, allegedly stored in the database
 * @param formalName the name used by Spring Security. It as a default prefix: 'ROLE_'
 */
public enum Roles {
    CO_OKR_ADMIN(Roles.CO_OKR_ADMIN_NAME),
    BUO_OKR_ADMIN(Roles.BUO_OKR_ADMIN_NAME),
    READ_ONLY_USER(Roles.READ_ONLY_USER_NAME);

    public static final String CO_OKR_ADMIN_NAME = "CO_OKR_Admin";
    public static final String BUO_OKR_ADMIN_NAME = "BUO_OKR_Admin";
    public static final String READ_ONLY_USER_NAME = "READ_Only_User";

    private String name;
    Roles(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public String getFormalName() {
        return SecurityConstants.ROLE_PREFIX + this.name;
    }
    
    
    @Override
    public String toString() {
        return this.getFormalName();
    }

}
