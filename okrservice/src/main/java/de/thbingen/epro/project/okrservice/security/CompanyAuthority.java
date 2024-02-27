package de.thbingen.epro.project.okrservice.security;

import org.springframework.http.HttpMethod;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyAuthority {
    
    private AuthorityString authorityString = new AuthorityString();
    private boolean shouldOwnObjective = false;
    private boolean shouldBeUserHimself = false;
    private HttpMethod[] methods = HttpMethod.values();

    public CompanyAuthority(AuthorityString authorityString, boolean shouldOwnObjective, boolean shouldBeUserHimself, HttpMethod ... methods) {
        this.authorityString = authorityString;
        this.shouldOwnObjective = shouldOwnObjective;
        this.shouldBeUserHimself = shouldBeUserHimself;
        this.methods = methods;
    }
    public CompanyAuthority(AuthorityString authorityString) {
        this.authorityString = authorityString;
    }
    public CompanyAuthority(AuthorityString authorityString, HttpMethod ... methods) {
        this.authorityString = authorityString;
        this.methods = methods;
    }

    
    public CompanyAuthority shouldBeUserHimself(boolean value) {
        this.shouldBeUserHimself = value;
        return this;
    }

    public CompanyAuthority allowMethods(HttpMethod ... methods) {
        this.methods = methods;
        return this;
    }

    public CompanyAuthority ownsObjective(boolean value) {
        this.shouldOwnObjective = value;
        return this;
    }

}
