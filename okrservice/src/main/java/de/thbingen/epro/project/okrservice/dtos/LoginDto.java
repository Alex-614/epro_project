package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull
    private String email;
    @NotNull
    private String password;

}
