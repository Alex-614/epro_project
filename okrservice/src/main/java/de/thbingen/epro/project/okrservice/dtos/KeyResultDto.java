package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.constants.KeyResultTypes;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyResultDto {
    private Long id;

    private Integer goal;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    private Integer current;

    @NotNull
    private Integer confidenceLevel;

    @NotEmpty
    @Pattern(regexp = KeyResultTypes.NUMERIC_NAME + "|" + KeyResultTypes.PERCENTUAL_NAME + "|" + KeyResultTypes.BINARY_NAME)
    private String type;


    @Valid
    @NotNull
    private List<UnitId> contributingUnits;
    
    @Valid
    @NotNull
    private List<BusinessUnitId> contributingBusinessUnits;


    public KeyResultDto(KeyResult keyResult) {
        this.id = keyResult.getId();
        this.goal = keyResult.getGoal();
        this.title = keyResult.getTitle();
        this.description = keyResult.getDescription();
        this.current = keyResult.getCurrent();
        this.confidenceLevel = keyResult.getConfidenceLevel();
        this.type = keyResult.getType().getName();
        this.contributingUnits = keyResult.getContributingUnits().stream().map(m -> m.getId()).collect(Collectors.toList());
        this.contributingBusinessUnits = keyResult.getContributingBusinessUnits().stream().map(m -> m.getId()).collect(Collectors.toList());
    }
}
