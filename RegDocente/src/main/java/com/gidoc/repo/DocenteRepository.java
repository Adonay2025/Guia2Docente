package com.gidoc.repo;

import com.gidoc.domain.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    Optional<Docente> findByEmail(String email);

    List<Docente> findByActivoTrue();

    List<Docente> findByEspecialidad(String especialidad);

    boolean existsByEmail(String email);
}