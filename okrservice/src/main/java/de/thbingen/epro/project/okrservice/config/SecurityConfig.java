package de.thbingen.epro.project.okrservice.config;

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

import de.thbingen.epro.project.okrservice.Roles;
import de.thbingen.epro.project.okrservice.jwt.JwtAuthEntryPoint;
import de.thbingen.epro.project.okrservice.jwt.JwtAuthenticationFilter;
import de.thbingen.epro.project.okrservice.repositories.RoleRepository;
import de.thbingen.epro.project.okrservice.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private RoleRepository roleRepository;

    private JwtAuthEntryPoint authEntryPoint;

    @SuppressWarnings("unused")
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, RoleRepository roleRepository) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.roleRepository = roleRepository;
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
                .requestMatchers(HttpMethod.POST, "/company/{companyId}/user/{userId}**")
                    .access(checkRoleAssignment(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers(HttpMethod.POST, "/company/{companyId}/buisinessunit")
                    .access(checkRoleAssignment(Roles.CO_OKR_ADMIN.getName()))
                .requestMatchers(HttpMethod.POST, "/company/{companyId}/buisinessunit/{buisinessUnitId}/unit")
                    .access(checkRoleAssignment(Roles.CO_OKR_ADMIN.getName()))
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private RoleAssignmentAuthorizationManager checkRoleAssignment(String role) {
        return new RoleAssignmentAuthorizationManager(getRoleIdByName(role));
    }

    private Long getRoleIdByName(String name) {
        return roleRepository.existsByName(name) ? roleRepository.findByName(name).getId() : 0;
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
