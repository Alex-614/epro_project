package de.thbingen.epro.project.okrservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.RoleAssignment;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.entities.company.Company;
import de.thbingen.epro.project.okrservice.entities.ids.RoleAssignmentId;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, RoleAssignmentId> {
    List<RoleAssignment> deleteByCompanyAndUserEquals(Company company, User user);
}
