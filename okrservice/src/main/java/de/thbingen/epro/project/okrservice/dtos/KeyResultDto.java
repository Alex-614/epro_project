package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.KeyResultTypes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class KeyResultDto {

    private int goal;
    
    @NotEmpty
    private String title;
    
    @NotEmpty
    private String description;
    
    private int current;
    
    @NotNull
    private int confidenceLevel;

    @Pattern(regexp = KeyResultTypes.NUMERIC_NAME + "|" + KeyResultTypes.PERCENTUAL_NAME + "|" + KeyResultTypes.BINARY_NAME)
    private String type;
    
}
