package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import lombok.Data;

@Data
public class AuthResponseDto {

    private String accessToken;
    private String tokenType = SecurityConstants.TOKEN_TYPE;

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

}
