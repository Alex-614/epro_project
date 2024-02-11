package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CompanyDto {

    private Long id;
    @NotEmpty(message = "'name' cannot be empty")
    private String name;


}
