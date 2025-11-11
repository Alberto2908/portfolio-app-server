package com.porftolio.alberto.services.impl;

import com.porftolio.alberto.models.Habilidad;
import com.porftolio.alberto.repositories.HabilidadRepository;
import com.porftolio.alberto.services.HabilidadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HabilidadServiceImpl implements HabilidadService {

    private final HabilidadRepository repository;

    public HabilidadServiceImpl(HabilidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Habilidad create(Habilidad habilidad) {
        return repository.save(habilidad);
    }

    @Override
    public Page<Habilidad> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Habilidad> listByCategory(String category, Pageable pageable) {
        return repository.findAllByCategory(category, pageable);
    }

    @Override
    public Optional<Habilidad> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Habilidad update(String id, Habilidad habilidad) {
        Habilidad current = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habilidad no encontrada: " + id));
        String oldImage = current.getImage();
        current.setName(habilidad.getName());
        current.setImage(habilidad.getImage());
        current.setCategory(habilidad.getCategory());
        Habilidad saved = repository.save(current);
        // If image changed, try to delete old file
        if (oldImage != null && !oldImage.isBlank() && !oldImage.equals(saved.getImage())) {
            deleteLocalUploadIfExists(oldImage);
        }
        return saved;
    }

    @Override
    public void delete(String id) {
        repository.findById(id).ifPresent(h -> {
            String img = h.getImage();
            repository.deleteById(id);
            if (img != null && !img.isBlank()) {
                deleteLocalUploadIfExists(img);
            }
        });
    }

    private void deleteLocalUploadIfExists(String imageUrl) {
        try {
            String pathPart = imageUrl;
            int idx = imageUrl.indexOf("/uploads/");
            if (idx >= 0) {
                pathPart = imageUrl.substring(idx + "/uploads/".length());
            } else if (imageUrl.startsWith("/uploads/")) {
                pathPart = imageUrl.substring("/uploads/".length());
            } else {
                // Not an uploads file, ignore
                return;
            }
            Path uploadDir = Paths.get("uploads");
            Path target = uploadDir.resolve(pathPart);
            if (Files.exists(target)) {
                Files.delete(target);
            }
        } catch (Exception ignored) {
            // Swallow exceptions to avoid failing the request due to FS issues
        }
    }
}
