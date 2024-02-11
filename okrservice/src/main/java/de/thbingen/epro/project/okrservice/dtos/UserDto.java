package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotEmpty(message = "'email' cannot be empty")
    private String email;

    @NotEmpty(message = "'password' cannot be empty")
    private String password;

    @NotEmpty(message = "'username' cannot be empty")
    private String username;

    @NotEmpty(message = "'firstname' cannot be empty")
    private String firstname;

    @NotEmpty(message = "'surname' cannot be empty")
    private String surname;






}
