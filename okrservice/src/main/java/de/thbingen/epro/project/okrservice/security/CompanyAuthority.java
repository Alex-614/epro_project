package de.thbingen.epro.project.okrservice.security;

import org.springframework.http.HttpMethod;

import lombok.Getter;

@Getter
public class CompanyAuthority {
    
    private AuthorityString authorityString;
    private boolean shouldOwnObjective = false;
    private HttpMethod[] methods = HttpMethod.values();

    public CompanyAuthority(AuthorityString authorityString, boolean shouldOwnObjective, HttpMethod ... methods) {
        this.authorityString = authorityString;
        this.shouldOwnObjective = shouldOwnObjective;
        this.methods = methods;
    }
    public CompanyAuthority(AuthorityString authorityString) {
        this.authorityString = authorityString;
    }
    public CompanyAuthority(AuthorityString authorityString, HttpMethod ... methods) {
        this.authorityString = authorityString;
        this.methods = methods;
    }

    
    public CompanyAuthority forMethods(HttpMethod ... methods) {
        this.methods = methods;
        return this;
    }

    public CompanyAuthority ownsObjective(boolean value) {
        this.shouldOwnObjective = value;
        return this;
    }

}
