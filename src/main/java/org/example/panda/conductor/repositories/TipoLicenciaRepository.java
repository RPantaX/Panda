package org.example.panda.conductor.repositories;

import org.example.panda.conductor.entities.TipoLicencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLicenciaRepository extends JpaRepository<TipoLicencia, Long> {
}
