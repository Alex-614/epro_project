package de.thbingen.epro.project.okrservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            // "Email already exists!"
            userDto.setEmail("Email already exists!");
            return new ResponseEntity<UserDto>(userDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setSurname(userDto.getSurname());

        user = userRepository.save(user);

        userDto.setId(user.getId());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable @NonNull Number userId) throws Exception {
        User user = Utils.getUserFromRepository(userRepository, userId.longValue());
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @PatchMapping("{userId}")
    public ResponseEntity<UserDto> patchUser(@PathVariable @NonNull Number userId, @RequestBody UserDto userDto) throws Exception {
        User user = Utils.getUserFromRepository(userRepository, userId);

        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getFirstname() != null) user.setFirstname(userDto.getFirstname());
        if (userDto.getSurname() != null) user.setSurname(userDto.getSurname());

        userRepository.save(user);
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }


    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable @NonNull Number userId) throws Exception {
        User user = Utils.getUserFromRepository(userRepository, userId.longValue());
        userRepository.deleteById(user.getId());
        return new ResponseEntity<>(user.getUsername() + " deleted!", HttpStatus.OK);
    }










    @GetMapping("{userId}/companies")
    public ResponseEntity<List<CompanyDto>> getUserCompanies(@PathVariable @NonNull Number userId) throws Exception {
        User user = Utils.getUserFromRepository(userRepository, userId.longValue());
        List<CompanyDto> dtos = new ArrayList<>();
        for (Company c : user.getCompanies()) {
            dtos.add(new CompanyDto(c));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }












}
