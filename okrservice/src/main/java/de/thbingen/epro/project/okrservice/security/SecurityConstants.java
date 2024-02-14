package de.thbingen.epro.project.okrservice.security;

public class SecurityConstants {

    public static final long JWT_EXPIRATION = 360000; // milliseconds (5min)

    public static final String JWT_SECRET = "superdupersecretsecretkeyforthisastonishingokrbackendsystemthatnooneknows";

    public static final String ROLE_PREFIX = "ROLE_"; // ROLE_ is the standard spring security prefix for roles
    
    public static final String TOKEN_TYPE = "Bearer ";

}
