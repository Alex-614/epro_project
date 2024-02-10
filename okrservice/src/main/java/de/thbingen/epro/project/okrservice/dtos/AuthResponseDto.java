package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.config.SecurityConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthResponseDto {

    @NotNull
    private String accessToken;
    @NotNull
    private String tokenType = SecurityConstants.TOKEN_TYPE;

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

}
