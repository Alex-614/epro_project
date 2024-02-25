package de.thbingen.epro.project.okrservice.services;

import java.util.List;

import de.thbingen.epro.project.okrservice.dtos.AuthResponseDto;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.LoginDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.exceptions.UserAlreadyExistsException;
import de.thbingen.epro.project.okrservice.exceptions.UserNotFoundException;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;
import de.thbingen.epro.project.okrservice.security.jwt.JwtGenerator;


/**
 * 
 * provides methods to interact with a User based on a UserRepository
 * 
 * @see User
 * @see UserRepository
 */
public interface UserService {


    /**
     * Find an User by its email.
     * 
     * @param email used to search for the User
     * @return the User 
     * @throws UserNotFoundException if the User was not found
     */
    User findUserByEmail(String email) throws UserNotFoundException;



    /**
     * Registers/Creates a new User in the repository
     * 
     * @param userDto contains data to create the User 
     * @return the User as UserDto
     * @throws UserAlreadyExistsException if a User with the same Email already exists
     * 
     * @see UserDto
     */
    UserDto register(UserDto userDto) throws UserAlreadyExistsException;



    /**
     * Find an User by its ID.
     * 
     * @param userId unique identifier used to search for the User
     * @return the User 
     * @throws UserNotFoundException if the User was not found
     */
    User findUser(long userId) throws UserNotFoundException;



    /**
     * <h3>User Authentication</h3>
     * Creates a token for Spring Security. The token can be use for further requests that need to be authorized.
     * 
     * @param loginDto contains the credentials
     * @return a AuthResponseDto
     * 
     * @see LoginDto
     * @see AuthResponseDto
     * @see JwtGenerator
     */
    AuthResponseDto login(LoginDto loginDto);



    /**
     * Find all Users from a repository.
     * Never returns null.
     * 
     * @return a list of the Users as UserDto 
     * 
     * @see UserDto
     */
    List<UserDto> findAllUsers();


    /**
     * 
     * Changes a User.
     * Only valid values will be changed.
     * 
     * @param userId the unique idetifier to adress the user that will be updated
     * @param userDto contains the updated data, that will be changed
     * @return the corresponding UserDto
     * @throws UserNotFoundException if the user is not found
     */
    UserDto patchUser(long userId, UserDto userDto) throws UserNotFoundException;



    /**
     * Deletes a User from a repository based on its ID.
     * 
     * If the User was not found it is silently ignored.
     * 
     * @param userId unique identifier of the User that needs to be deleted.
     */
    void deleteUser(long userId);



    /**
     * Find all Companies related to the User.
     * 
     * @param userId unique identifier used to search for the User
     * @return a List of the Companies
     * @throws UserNotFoundException if the User was not found
     */
    List<CompanyDto> findUserCompanies(long userId) throws UserNotFoundException;


}
