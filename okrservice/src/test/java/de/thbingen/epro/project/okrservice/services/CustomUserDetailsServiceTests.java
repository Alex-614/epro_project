package de.thbingen.epro.project.okrservice.services;

import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.services.impl.CustomUserDetailsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTests {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void CustomUserDetailsService_LoadUserByUsername_ReturnUserDetails() {
        User user = User.builder()
                .email("test@test.de")
                .password("test")
                .roleAssignments(new ArrayList<>())
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@test.de")
                .password("test")
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails foundUserDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        Assertions.assertThat(foundUserDetails).isEqualTo(userDetails);
    }
}
