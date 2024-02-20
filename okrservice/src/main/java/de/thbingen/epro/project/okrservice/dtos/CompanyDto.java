package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.Company;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    
    @NotEmpty(message = "'name' cannot be empty")
    private String name;

    public CompanyDto(Company company) {
        this.id = company.getId();
        this.name = company.getName();
    }
}
