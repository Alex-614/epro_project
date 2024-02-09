package de.thbingen.epro.project.okrservice.dtos;

import de.thbingen.epro.project.okrservice.entities.ids.BuisinessUnitId;
import lombok.Data;

@Data
public class BuisinessUnitDto {

    private BuisinessUnitId id;
    private String name;

}
