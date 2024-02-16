package de.thbingen.epro.project.okrservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorityString {

    /*
     *   Format: <ROLE_><privilege>_<companyId>_<businessUnitId>
     */

    public static AuthorityString Role(String authority, String companyId) {
        return new AuthorityString(true, authority, companyId, "");
    }
    public static AuthorityString Privilege(String authority, String companyId) {
        return new AuthorityString(false, authority, companyId, "");
    }


    private static final String seperator = "_";

    private boolean isRole;
    private String authority;
    private String companyId;
    private String businessUnitId;
    
    public AuthorityString fromString(String authorityString) {
        boolean isRole = authorityString.startsWith(SecurityConstants.ROLE_PREFIX);

        String businessUnitId = authorityString.substring(authorityString.lastIndexOf(seperator) + 1);

        authorityString = authorityString.substring(0, authorityString.lastIndexOf(seperator));
        String companyId = authorityString.substring(authorityString.lastIndexOf(seperator) + 1);
        
        authorityString = authorityString.substring(0, authorityString.lastIndexOf(seperator));
        if (isRole) {
            authorityString = authorityString.substring(SecurityConstants.ROLE_PREFIX.length());
        }
        return new AuthorityString(isRole, authorityString, companyId, businessUnitId);
    }

    @Override
    public String toString() {
        String authorityString = "";
        if (isRole) {
            authorityString += SecurityConstants.ROLE_PREFIX;
        }
        authorityString += String.format("%s%s%s%s%s", 
                                            authority, 
                                            seperator,
                                            (companyId != null ? companyId : ""), 
                                            seperator,
                                            (businessUnitId != null ? businessUnitId : ""));
        return authorityString;
    }



}
