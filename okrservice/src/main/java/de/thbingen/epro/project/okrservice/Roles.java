package de.thbingen.epro.project.okrservice;

import de.thbingen.epro.project.okrservice.security.SecurityConstants;

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
