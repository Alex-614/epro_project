package de.thbingen.epro.project.okrservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <p>Used to map from Role/Privilege names to the actually used Authorities</p>
 * 
 * StringFormat: {prefix}{privilege}_{companyId}_{businessUnitId};
 * if no value is set the String will be empty.
 * 
 * <ui>
 *   <li>- the default Spring security prefix is 'ROLE_'; it will be added if 'isRole' == true</li>
 *   <li>- privilege will be replaced with authority</li>
 * </ui>
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityString {

    /**
     * Creates an AuthorityString based on a Role name
     * 
     * @param authority the name of the role
     * @param companyId 
     * @return the Role as AuthorityString
     */
    public static AuthorityString Role(String authority, String companyId) {
        return new AuthorityString(true, authority, companyId, "");
    }
    
    
    /**
     * Creates an AuthorityString based on a Privilege name
     * 
     * @param authority the name of the privilege
     * @param companyId 
     * @return the Privilege as AuthorityString
     */
    public static AuthorityString Privilege(String authority, String companyId) {
        return new AuthorityString(false, authority, companyId, "");
    }


    private static final String seperator = "_";

    private boolean isRole = false;
    private String authority = "";
    private String companyId = "";
    private String businessUnitId = "";
    
    /**
     * Only works properly, if at least one Attribure, except 'isRole', is set
     */
    @Deprecated
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
        String tempAuthority = authority != null ? authority : "";
        String tempCompanyId = companyId != null ? companyId : "";
        String tempBusinessUnitId = businessUnitId != null ? businessUnitId : "";
        if (isRole) {
            authorityString += SecurityConstants.ROLE_PREFIX;
        } else {
            if (tempAuthority.isEmpty() && tempCompanyId.isEmpty() && tempBusinessUnitId.isEmpty()) {
                return "";
            }
        }
        authorityString += String.format("%s%s%s%s%s", 
                                            tempAuthority, 
                                            seperator,
                                            tempCompanyId, 
                                            seperator,
                                            tempBusinessUnitId);
        return authorityString;
    }



}
