package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.security.SecurityConstants;
import lombok.Data;

@Data
public class AuthResponseDto {

    private String accessToken;
    private String tokenType = SecurityConstants.TOKEN_TYPE;
    private Long expirationTime;
    
    private Long userId;
    
    public AuthResponseDto(String accessToken, Long expirationTime, Long userId) {
        this.accessToken = accessToken;
        this.expirationTime = expirationTime;
        this.userId = userId;
    }

}
