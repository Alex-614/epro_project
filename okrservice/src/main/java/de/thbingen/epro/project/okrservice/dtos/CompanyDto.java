package de.thbingen.epro.project.okrservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyDto {

    private Long id;
    @NotNull
    private String name;


}
