package com.porftolio.alberto.services;

import com.porftolio.alberto.models.Habilidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HabilidadService {
    Habilidad create(Habilidad habilidad);
    Page<Habilidad> list(Pageable pageable);
    Page<Habilidad> listByCategory(String category, Pageable pageable);
    Optional<Habilidad> findById(String id);
    Habilidad update(String id, Habilidad habilidad);
    void delete(String id);
}
