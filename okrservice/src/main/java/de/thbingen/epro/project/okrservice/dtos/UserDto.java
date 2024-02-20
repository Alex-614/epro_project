package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty(message = "'email' cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "'password' cannot be empty")
    private String password;

    @NotEmpty(message = "'username' cannot be empty")
    private String username;

    @NotEmpty(message = "'firstname' cannot be empty")
    private String firstname;

    @NotEmpty(message = "'surname' cannot be empty")
    private String surname;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.surname = user.getSurname();
    }
}
