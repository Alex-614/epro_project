package de.thbingen.epro.project.okrservice.controller;

import java.lang.reflect.Field;
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

import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

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
        User oldUser = Utils.getUserFromRepository(userRepository, userId);
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


    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable @NonNull Number userId) throws Exception {
        User user = Utils.getUserFromRepository(userRepository, userId.longValue());
        userRepository.deleteById(user.getId());
        return new ResponseEntity<>(user.getUsername() + " deleted!", HttpStatus.OK);
    }






}
