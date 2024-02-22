package de.thbingen.epro.project.okrservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import de.thbingen.epro.project.okrservice.Privileges;
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

                        // register
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        // login
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        /*
                         *  CO_OKR_ADMIN
                         */
                        // add User to Company
                        .requestMatchers(HttpMethod.POST, "/company/{companyId}/user/{userId}*")
                                .access(hasAnyCompanyAuthority(
                                        hasRole(Roles.CO_OKR_ADMIN)))

                        // post business unit objective
                        /*.requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST, HttpMethod.GET)))

                        // patch business unit objective
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE)))


                        // post company objective
                        .requestMatchers("/company/{companyId}/objective")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST, HttpMethod.GET)))

                        // patch company objective
                        .requestMatchers("/company/{companyId}/objective/{objectiveId}")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE)))

                        // post business unit
                        .requestMatchers("/company/{companyId}/businessunit")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST, HttpMethod.GET)))

                        // patch business unit
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE)))*/

                        // post company
                        /*.requestMatchers("/company")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST, HttpMethod.GET)))

                        // patch company
                        .requestMatchers("/company/{companyId}")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE)))*/

                        // create Unit
                        /*.requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/unit")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST)))

                        // CompanyObjective
                        .requestMatchers("/company/{companyId}/objective")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST)))

                        // CompanyKeyResult
                        .requestMatchers("/company/{companyId}/keyresult")
                        .access(hasAnyCompanyAuthority(
                                hasRole(Roles.CO_OKR_ADMIN)
                                        .forMethods(HttpMethod.POST)))*/


                        /*
                         *  BUO_OKR_ADMIN
                         */
                        // BusinessUnitObjective
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective")
                                .access(hasAnyCompanyAuthority(
                                                hasRole(Roles.BUO_OKR_ADMIN)
                                                        .forMethods(HttpMethod.POST)))

                        // BusinessUnitKeyResult
                        .requestMatchers("/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}/keyresult")
                                .access(hasAnyCompanyAuthority(
                                                hasRole(Roles.BUO_OKR_ADMIN)
                                                        .ownsObjective(true)
                                                        .forMethods(HttpMethod.POST)))


                        /*
                         *  READ_ONLY_USER
                         */
                        // CompanyObjective
                        .requestMatchers(HttpMethod.GET, "/company/{companyId}/objective/{objectiveId}")
                                .access(hasAnyCompanyAuthority(
                                                hasRole(Roles.READ_ONLY_USER)))

                        // BusinessUnitObjective
                        .requestMatchers(HttpMethod.GET, "/company/{companyId}/businessunit/{businessUnitId}/objective/{objectiveId}")
                                .access(hasAnyCompanyAuthority(
                                                hasRole(Roles.READ_ONLY_USER)))


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
