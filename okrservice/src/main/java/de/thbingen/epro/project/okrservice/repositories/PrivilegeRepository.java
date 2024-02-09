package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    
}
