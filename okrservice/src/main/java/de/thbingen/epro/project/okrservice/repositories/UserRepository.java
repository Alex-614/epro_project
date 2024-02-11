package de.thbingen.epro.project.okrservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thbingen.epro.project.okrservice.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByEmail(String email);
    boolean existsByEmail(String email);
    
    //static final String EXP_STRING = "SELECT id FROM tbl_objective WHERE user_id = ?1";
    //@Query(value = EXP_STRING, nativeQuery = true)
    //List<Long> findOwnedBuisinessUnitIdsById(Long id);
    
}
