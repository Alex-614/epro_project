package de.thbingen.epro.project.okrservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.thbingen.epro.project.okrservice.AuthorityString;
import de.thbingen.epro.project.okrservice.Roles;
import de.thbingen.epro.project.okrservice.jwt.JwtAuthEntryPoint;
import de.thbingen.epro.project.okrservice.jwt.JwtAuthenticationFilter;
import de.thbingen.epro.project.okrservice.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;

    @SuppressWarnings("unused")
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authEntryPoint))
            .sessionManagement(manager -> manager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/company/{companyId}/user/{userId}*")
                    .access(hasRole(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers("/company/{companyId}/buisinessunit")
                    .access(hasRole(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers("/company/{companyId}/buisinessunit/{buisinessUnitId}/unit")
                    .access(hasRole(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers("/company/{companyId}/objective")
                    .access(hasRole(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers("/company/{companyId}/buisinessunit/{buisinessUnitId}/objective")
                    .access(hasRole(Roles.BUO_OKR_ADMIN.getName()))
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public CompanyAuthorizationManager hasRole(String role) {
        return authorizationManager(AuthorityString.Role(role, "{companyId}"), false);
    }
    public CompanyAuthorizationManager hasRoleAndOwnsObjective(String role) {
        return authorizationManager(AuthorityString.Role(role, "{companyId}"), true);
    }
    
    public CompanyAuthorizationManager hasPrivilege(String privilege) {
        return authorizationManager(AuthorityString.Privilege(privilege, "{companyId}"), false);
    }
    public CompanyAuthorizationManager hasPrivilegeAndOwnsObjective(String privilege) {
        return authorizationManager(AuthorityString.Privilege(privilege, "{companyId}"), true);
    }
    
    @Bean
    @Scope("prototype")
    public CompanyAuthorizationManager authorizationManager(AuthorityString authorityString, boolean shouldOwnObjective) {
        return new CompanyAuthorizationManager(authorityString, shouldOwnObjective);
    }









    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    

}
