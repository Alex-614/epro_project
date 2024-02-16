package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.ids.KeyResultUpdateId;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultUpdate;

public interface KeyResultUpdateRepository extends JpaRepository<KeyResultUpdate, KeyResultUpdateId> {

    

}
