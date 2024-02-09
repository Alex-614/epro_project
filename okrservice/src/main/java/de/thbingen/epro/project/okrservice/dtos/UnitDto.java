package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.UnitId;
import lombok.Data;

@Data
public class UnitDto {

    private UnitId id;
    private String name;


}
