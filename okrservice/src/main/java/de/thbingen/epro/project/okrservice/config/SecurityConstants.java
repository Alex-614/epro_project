package de.thbingen.epro.project.okrservice.config;

public class SecurityConstants {

    public static final long JWT_EXPIRATION = 360000; // milliseconds (5min)

    public static final String JWT_SECRET = "superdupersecretsecretkeyforthisastonishingokrbackendsystemthatnooneknows";

    public static final String ROLE_PREFIX = "ROLE_";
    
    public static final String TOKEN_TYPE = "Bearer ";

}
