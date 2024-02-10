package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;
    @NotNull
    private String firstname;
    @NotNull
    private String surname;






}
