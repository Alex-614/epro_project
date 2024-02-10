package de.thbingen.epro.project.okrservice;

import de.thbingen.epro.project.okrservice.config.SecurityConstants;

public enum Roles {
    CO_OKR_ADMIN("CO_OKR_Admin"),
    BUO_OKR_ADMIN("BUO_OKR_Admin"),
    READ_ONLY_USER("READ_Only_User");

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
