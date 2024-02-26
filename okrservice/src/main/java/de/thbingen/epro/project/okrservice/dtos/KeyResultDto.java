package de.thbingen.epro.project.okrservice.dtos;

import java.util.List;
import java.util.stream.Collectors;

import de.thbingen.epro.project.okrservice.constants.KeyResultTypes;
import de.thbingen.epro.project.okrservice.entities.ids.BusinessUnitId;
import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class KeyResultDto {

    private Long id;

    @PositiveOrZero
    private Integer goal;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @PositiveOrZero
    private Integer current;

    @NotNull
    private Integer confidenceLevel;

    @NotBlank
    @Pattern(regexp = KeyResultTypes.NUMERIC_NAME + "|" + KeyResultTypes.PERCENTUAL_NAME + "|" + KeyResultTypes.BINARY_NAME)
    private String type;

    @PositiveOrZero
    private Long objectiveId;

    @Valid
    @NotNull
    private List<UnitId> contributingUnits;
    
    @Valid
    @NotNull
    private List<BusinessUnitId> contributingBusinessUnits;

    @PositiveOrZero
    private Float achievement;

    public KeyResultDto(KeyResult keyResult) {
        this.id = keyResult.getId();
        this.goal = keyResult.getGoal();
        this.title = keyResult.getTitle();
        this.description = keyResult.getDescription();
        this.current = keyResult.getCurrent();
        this.confidenceLevel = keyResult.getConfidenceLevel();
        this.type = keyResult.getType().getName();
        this.objectiveId = keyResult.getObjective().getId();
        this.contributingUnits = keyResult.getContributingUnits().stream().map(m -> m.getId()).collect(Collectors.toList());
        this.contributingBusinessUnits = keyResult.getContributingBusinessUnits().stream().map(m -> m.getId()).collect(Collectors.toList());
        this.achievement = keyResult.getAchievement();
    }
}
