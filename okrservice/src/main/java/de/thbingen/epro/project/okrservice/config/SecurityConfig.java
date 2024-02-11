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
                        hasRole(Roles.CO_OKR_ADMIN.getName())))

                // create BuisinessUnit
                .requestMatchers("/company/{companyId}/buisinessunit")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.CO_OKR_ADMIN.getName())
                            .forMethods(HttpMethod.POST, HttpMethod.DELETE)))

                // create Unit
                .requestMatchers("/company/{companyId}/buisinessunit/{buisinessUnitId}/unit")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.CO_OKR_ADMIN.getName())
                            .forMethods(HttpMethod.POST, HttpMethod.DELETE)))

                // CompanyObjective
                .requestMatchers("/company/{companyId}/objective")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.CO_OKR_ADMIN.getName())
                           .forMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE)))
                    
                // CompanyKeyResult
                .requestMatchers("/company/{companyId}/keyresult")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.CO_OKR_ADMIN.getName())
                            .forMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE)))
                
                
                /*
                 *  BUO_OKR_ADMIN
                 */
                // BuisinessUnitObjective
                .requestMatchers("/company/{companyId}/buisinessunit/{buisinessUnitId}/objective")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.BUO_OKR_ADMIN.getName())
                            .ownsObjective(true)
                            .forMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE)))

                // BuisinessUnitKeyResult
                .requestMatchers("/company/{companyId}/buisinessunit/{buisinessUnitId}/keyresult")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.BUO_OKR_ADMIN.getName())
                            .ownsObjective(true)
                            .forMethods(HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE)))
                

                /*
                 *  READ_ONLY_USER
                 */
                // CompanyObjective
                .requestMatchers(HttpMethod.GET, "/company/{companyId}/objective/{objectiveId}")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.READ_ONLY_USER.getName())))
                
                // BuisinessUnitObjective
                .requestMatchers(HttpMethod.GET, "/company/{companyId}/objective/buisinessunit/{buisinessUnitId}/objective/{objectiveId}")
                    .access(hasAnyCompanyAuthority(
                        hasRole(Roles.READ_ONLY_USER.getName())))


                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    protected CompanyAuthority hasRole(String role) {
        return new CompanyAuthority(AuthorityString.Role(role, "{companyId}"));
    }
    protected CompanyAuthority hasPrivilege(String privilege) {
        return new CompanyAuthority(AuthorityString.Privilege(privilege, "{companyId}"));
    }
    @Bean
    @Scope("prototype")
    public CompanyAuthorizationManager hasAnyCompanyAuthority(CompanyAuthority ... companyAuthorities) {
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
