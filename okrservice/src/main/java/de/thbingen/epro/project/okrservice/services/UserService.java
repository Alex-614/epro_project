package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.exceptions.UserAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;

public interface UserService {


    User findUserByEmail(String email) throws UserNotFoundException;
    UserDto register(UserDto userDto) throws UserAlreadyExistsException;
    User findUser(long userId) throws UserNotFoundException;
    AuthResponseDto login(LoginDto loginDto);
    List<UserDto> findAllUsers();
    UserDto patchUser(long userId, UserDto userDto) throws UserNotFoundException;
    void deleteUser(long userId);
    List<CompanyDto> findUserCompanies(long userId) throws UserNotFoundException;


}
