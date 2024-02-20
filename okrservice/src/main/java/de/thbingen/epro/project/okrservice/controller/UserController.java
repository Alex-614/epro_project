package de.thbingen.epro.project.okrservice.controller;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.jwt.JwtGenerator;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository, 
                            PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }


    @PostMapping("/user")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
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

        userRepository.save(user);

        userDto.setId(user.getId());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserDto> patchUser(@PathVariable @NonNull Number userId, @RequestBody UserDto userDto) throws Exception {
        User oldUser = Helper.getUserFromRepository(userRepository, userId);
        UserDto oldUserDto = new UserDto(oldUser);

        Field[] fields = UserDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(userDto);
            if(value != null) {
                field.set(oldUserDto, value);
            }
            field.setAccessible(false);
        }

        oldUser.setEmail(oldUserDto.getEmail());
        oldUser.setPassword(oldUserDto.getPassword());
        oldUser.setUsername(oldUserDto.getUsername());
        oldUser.setFirstname(oldUserDto.getFirstname());
        oldUser.setSurname(oldUserDto.getSurname());

        userRepository.save(oldUser);
        return new ResponseEntity<>(oldUserDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable @NonNull Number userId) throws Exception {
        User user = Helper.getUserFromRepository(userRepository, userId.longValue());
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable @NonNull Number userId) throws Exception {
        User user = Helper.getUserFromRepository(userRepository, userId.longValue());
        userRepository.deleteById(user.getId());
        return new ResponseEntity<>(user.getUsername() + " deleted!", HttpStatus.OK);
    }


    /*
     * Authentication
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                loginDto.getEmail(), 
                                                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }





}
