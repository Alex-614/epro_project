package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.company.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
