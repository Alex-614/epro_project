package de.thbingen.epro.project.okrservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.services.UserService;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {


    private UserService userService;


    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        AuthResponseDto authDto = userService.login(loginDto);
        return new ResponseEntity<>(authDto, HttpStatus.OK);
    }


}
