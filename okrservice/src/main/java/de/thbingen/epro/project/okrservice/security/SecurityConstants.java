package de.thbingen.epro.project.okrservice.security;

/**
 * Configuratable security values for the application
 */
public class SecurityConstants {

    public static final long JWT_EXPIRATION = 360000; // milliseconds (5min)

    public static final String JWT_SECRET = "superdupersecretsecretkeyforthisastonishingokrbackendsystemthatnooneknows";

    public static final String TOKEN_TYPE = "Bearer ";
    
    public static final String ROLE_PREFIX = "ROLE_"; // ROLE_ is the defauld spring security prefix for roles

}
