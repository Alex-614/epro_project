package de.thbingen.epro.project.okrservice;

import de.thbingen.epro.project.okrservice.config.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorityString {

    /*
     *   Format: <ROLE_><privilege>_<companyId>_<buisinessUnitId>
     */

    public static AuthorityString Role(String authority, String companyId) {
        return new AuthorityString(true, authority, companyId, "");
    }
    public static AuthorityString Privilege(String authority, String companyId) {
        return new AuthorityString(false, authority, companyId, "");
    }




    private boolean isRole;
    private String authority;
    private String companyId;
    private String buisinessUnitId;
    
    public AuthorityString fromString(String authorityString) {
        boolean isRole = authorityString.startsWith(SecurityConstants.ROLE_PREFIX);

        String buisinessUnitId = authorityString.substring(authorityString.lastIndexOf("_") + 1);

        authorityString = authorityString.substring(0, authorityString.lastIndexOf("_"));
        String companyId = authorityString.substring(authorityString.lastIndexOf("_") + 1);
        
        authorityString = authorityString.substring(0, authorityString.lastIndexOf("_"));
        if (isRole) {
            authorityString = authorityString.substring(SecurityConstants.ROLE_PREFIX.length());
        }
        return new AuthorityString(isRole, authorityString, companyId, buisinessUnitId);
    }

    @Override
    public String toString() {
        String authorityString = "";
        if (isRole) {
            authorityString += SecurityConstants.ROLE_PREFIX;
        }
        authorityString += String.format("%s_%s_%s", 
        authority, 
        (companyId != null ? companyId.toString() : ""), 
        (buisinessUnitId != null ? buisinessUnitId.toString() : ""));
        return authorityString;
    }



}
