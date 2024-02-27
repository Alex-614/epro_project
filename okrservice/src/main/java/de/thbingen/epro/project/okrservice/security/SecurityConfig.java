package de.thbingen.epro.project.okrservice.security;

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

import de.thbingen.epro.project.okrservice.constants.Privileges;
import de.thbingen.epro.project.okrservice.constants.Roles;
import de.thbingen.epro.project.okrservice.security.jwt.JwtAuthEntryPoint;
import de.thbingen.epro.project.okrservice.security.jwt.JwtAuthenticationFilter;
import de.thbingen.epro.project.okrservice.services.impl.CustomUserDetailsService;

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

                        // user
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/user/{userId}", "/user/{userId}/companies")
                                .access(hasAnyCompanyAuthority(
                                        new CompanyAuthority()
                                                .shouldBeUserHimself(true)))
                        // login
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()


                        // BusinessUnit
                        .requestMatchers("/company/{companyId}/businessunit", 
                                                "/company/{companyId}/businessunit/{businessUnitId}")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))
                                                
                        // Unit
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/unit", 
                                                "/company/{companyId}/businessunit/{businessUnitId}/unit/{unitId}")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.BUO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))


                        // add User to Company / remove User from Company
                        .requestMatchers(HttpMethod.POST, "/company/{companyId}/user/{userId}/add", 
                                                                "/company/{companyId}/user/{userId}/remove")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST)))


                        // CompanyObjective
                        .requestMatchers("/company/{companyId}/objective", 
                                                "/company/{companyId}/objective/{objectiveId}")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))

                        // CompanyKeyResult
                        .requestMatchers("/company/{companyId}/objective/{objectiveId}/keyresult", 
                                                "/company/{companyId}/objective/{objectiveId}/keyresult/{keyResultId}",
                                                "/company/{companyId}/objective/{objectiveId}/keyresult/{keyResultId}/contributing/units",
                                                "/company/{companyId}/objective/{objectiveId}/keyresult/{keyResultId}/contributing/businuessunits",
                                                "/company/{companyId}/objective/{objectiveId}/keyresult/{keyResultId}/updatehistory")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))

                        // BusinessUnitObjective
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective", 
                                                "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.BUO_OKR_ADMIN)
                                                .allowMethods(HttpMethod.POST), 
                                        hasRole(Roles.BUO_OKR_ADMIN)
                                                .ownsObjective(true)
                                                .allowMethods(HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))

                        // BusinessUnitKeyResult
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult", 
                                                "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}",
                                                "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}/contributing/units",
                                                "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}/contributing/businuessunits",
                                                "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult/{keyResultId}/updatehistory")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.BUO_OKR_ADMIN)
                                                .ownsObjective(true)
                                                .allowMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE), 
                                        hasRole(Roles.READ_ONLY_USER)
                                                .allowMethods(HttpMethod.GET)))


                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    protected CompanyAuthority hasRole(Roles role) {
        return hasRole(role.getName());
    }

    protected CompanyAuthority hasRole(String role) {
        return new CompanyAuthority(AuthorityString.Role(role, "{companyId}"));
    }

    protected CompanyAuthority hasPrivilege(Privileges privilege) {
        return hasPrivilege(privilege.getName());
    }

    protected CompanyAuthority hasPrivilege(String privilege) {
        return new CompanyAuthority(AuthorityString.Privilege(privilege, "{companyId}"));
    }

    @Bean
    @Scope("prototype")
    public CompanyAuthorizationManager hasAnyCompanyAuthority(CompanyAuthority... companyAuthorities) {
        return new CompanyAuthorizationManager(companyAuthorities);
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
