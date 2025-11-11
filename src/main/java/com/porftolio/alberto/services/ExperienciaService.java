package com.porftolio.alberto.services;

import com.porftolio.alberto.models.Experiencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExperienciaService {
    Page<Experiencia> list(Pageable pageable);
    Optional<Experiencia> findById(String id);
    Experiencia create(Experiencia experiencia);
    Experiencia update(String id, Experiencia experiencia);
    void delete(String id);
}
