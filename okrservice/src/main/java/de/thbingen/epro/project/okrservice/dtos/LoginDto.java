package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "'email' cannot be empty")
    private String email;
    @NotNull(message = "'password' cannot be empty")
    private String password;

}
