package de.thbingen.epro.project.okrservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.exceptions.UserAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import de.thbingen.epro.project.okrservice.security.jwt.JwtGenerator;
import de.thbingen.epro.project.okrservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {


    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                            AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }


    @Override
    public User findUser(long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }



    @Override
    public UserDto register(UserDto userDto) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setSurname(userDto.getSurname());

        user = userRepository.save(user);

        return user.toDto();
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                                    new UsernamePasswordAuthenticationToken(
                                        loginDto.getEmail(), 
                                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        User user = userRepository.findByEmail(loginDto.getEmail()).get();
        return new AuthResponseDto(token, SecurityConstants.JWT_EXPIRATION, user.getId());
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(m -> m.toDto()).collect(Collectors.toList());
    }

    @Override
    public UserDto patchUser(long userId, UserDto userDto) throws UserNotFoundException {
        User user = findUser(userId);
        
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) 
            if (!userRepository.existsByEmail(userDto.getEmail())) user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) user.setUsername(userDto.getUsername());
        if (userDto.getFirstname() != null && !userDto.getFirstname().isBlank()) user.setFirstname(userDto.getFirstname());
        if (userDto.getSurname() != null && !userDto.getSurname().isBlank()) user.setSurname(userDto.getSurname());
        
        userRepository.save(user);
        return user.toDto();
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<CompanyDto> findUserCompanies(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return user.getCompanies().stream().map(m -> m.toDto()).collect(Collectors.toList());
    }












}
