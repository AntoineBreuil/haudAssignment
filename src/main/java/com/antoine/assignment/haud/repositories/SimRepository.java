package com.antoine.assignment.haud.repositories;

import com.antoine.assignment.haud.entities.Sim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimRepository extends CrudRepository<Sim, Long> {
    Sim findByICCID(String iccId);
}
