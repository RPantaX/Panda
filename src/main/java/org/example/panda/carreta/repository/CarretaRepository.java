package org.example.panda.carreta.repository;

import org.example.panda.carreta.entity.Carreta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarretaRepository extends JpaRepository<Carreta, Integer> {
}
