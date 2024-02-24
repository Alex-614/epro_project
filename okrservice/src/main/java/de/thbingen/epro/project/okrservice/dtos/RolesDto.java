package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolesDto {

    @NotNull
    private List<Number> roleIds;



}
