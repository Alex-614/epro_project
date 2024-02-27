package de.thbingen.epro.project.okrservice.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import de.thbingen.epro.project.okrservice.security.jwt.JwtGenerator;
import de.thbingen.epro.project.okrservice.services.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtGenerator jwtGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void UserService_Register_ReturnCreated() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();
        UserDto userDto = user.toDto();
        userDto.setPassword(user.getPassword());

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto savedUser = userService.register(userDto);

        //Set password null because the dto which is used to create the user needs a password
        //but the service doesn't return the password for safety reasons
        userDto.setPassword(null);

        Assertions.assertThat(savedUser).isEqualTo(userDto);
    }

    @Test
    public void UserService_Login_ReturnAuthResponseDto() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(user.getEmail());
        loginDto.setPassword(user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        AuthResponseDto authResponseDto = new AuthResponseDto(token, SecurityConstants.JWT_EXPIRATION, 1L);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthResponseDto returnedAuthResponseDto = userService.login(loginDto);

        Assertions.assertThat(returnedAuthResponseDto).isEqualTo(authResponseDto);
    }

    @Test
    public void UserService_FindAllUsers_ReturnUserList() {
        ArrayList<User> users = new ArrayList<>();
        for(long i = 0L; i < 3L; i++) {
            User user = User.builder()
                    .id(i)
                    .username("testuser")
                    .email("test@test.de")
                    .password("test")
                    .firstname("my first name")
                    .surname("sur").build();
            users.add(user);
        }

        ArrayList<UserDto> usersDtos = users.stream()
                .map(User::toDto).collect(Collectors.toCollection(ArrayList::new));

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> foundUsers = userService.findAllUsers();

        Assertions.assertThat(foundUsers).isEqualTo(usersDtos);
    }

    @Test
    public void UserService_FindUser_ReturnUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.findUser(1L);

        Assertions.assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void UserService_FindUserByEmail_ReturnUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByEmail(user.getEmail());

        Assertions.assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void UserService_FindUserCompanies_ReturnUser() throws Exception {
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(Company.builder().name("test").build());

        ArrayList<CompanyDto> companieDtos = new ArrayList<>();
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("test");
        companieDtos.add(companyDto);

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur")
                .companies(companies)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<CompanyDto> foundCompanyDtos = userService.findUserCompanies(user.getId());

        Assertions.assertThat(foundCompanyDtos).isEqualTo(companieDtos);
    }

    @Test
    public void UserService_PatchUser_ReturnUserDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();

        UserDto userDto = user.toDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto patchedUser = userService.patchUser(1L, userDto);

        Assertions.assertThat(patchedUser).isEqualTo(userDto);
    }

    @Test
    public void UserService_DeleteUser_ReturnVoid() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();

        assertAll(() -> userService.deleteUser(user.getId()));
    }
}
