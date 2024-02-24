package de.thbingen.epro.project.okrservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.exceptions.UserAlreadyExistsException;
import de.thbingen.epro.project.okrservice.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) throws UserAlreadyExistsException {
        UserDto response = userService.register(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable @NonNull Number userId) throws Exception {
        UserDto response = userService.findUser(userId.longValue()).toDto();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> response = userService.findAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("{userId}")
    public ResponseEntity<UserDto> patchUser(@PathVariable @NonNull Number userId, @RequestBody UserDto userDto) throws Exception {
        UserDto response = userService.patchUser(userId.longValue(), userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Number userId) throws Exception {
        userService.deleteUser(userId.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @GetMapping("{userId}/companies")
    public ResponseEntity<List<CompanyDto>> getUserCompanies(@PathVariable @NonNull Number userId) throws Exception {
        List<CompanyDto> response = userService.findUserCompanies(userId.longValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }












}
