package de.thbingen.epro.project.okrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.jwt.JwtGenerator;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {


    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    private UserRepository userRepository;


    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, 
                                    UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                loginDto.getEmail(), 
                                                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        User user = userRepository.findByEmail(loginDto.getEmail());
        return new ResponseEntity<>(new AuthResponseDto(token, SecurityConstants.JWT_EXPIRATION, user.getId()), HttpStatus.OK);
    }


}
