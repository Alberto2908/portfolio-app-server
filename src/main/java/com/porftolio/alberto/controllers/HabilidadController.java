package com.porftolio.alberto.controllers;

import com.porftolio.alberto.models.Habilidad;
import com.porftolio.alberto.services.HabilidadService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/habilidades")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class HabilidadController {

    private final HabilidadService service;

    public HabilidadController(HabilidadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Habilidad> create(@Valid @RequestBody Habilidad body) {
        Habilidad saved = service.create(body);
        return ResponseEntity.created(URI.create("/api/habilidades/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<Habilidad>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category
    ) {
        Sort sort = Sort.by(Sort.Order.desc("level"), Sort.Order.asc("name"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Habilidad> result = (category == null || category.isBlank())
                ? service.list(pageable)
                : service.listByCategory(category, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habilidad> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habilidad> update(@PathVariable String id, @Valid @RequestBody Habilidad body) {
        Habilidad updated = service.update(id, body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // sanitize filename
        String original = file.getOriginalFilename();
        String filename = (original == null ? "file" : original).replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = uploadDir.resolve(filename);
        // If exists, add numeric suffix
        int i = 1;
        while (Files.exists(target)) {
            String base = filename;
            String ext = "";
            int dot = filename.lastIndexOf('.')
;            if (dot > 0) { base = filename.substring(0, dot); ext = filename.substring(dot); }
            target = uploadDir.resolve(base + "_" + (i++) + ext);
        }
        Files.copy(file.getInputStream(), target);
        String publicUrl = "/uploads/" + target.getFileName();
        Map<String, String> body = new HashMap<>();
        body.put("url", publicUrl);
        return ResponseEntity.ok(body);
    }
}
