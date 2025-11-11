package com.porftolio.alberto.controllers;

import com.porftolio.alberto.models.VisitCounter;
import com.porftolio.alberto.services.VisitCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = {
    "http://localhost:4200", 
    "https://alberto-cabello-portfolio.vercel.app",
    "https://alberto-cabello-portfolio-alberto2908s-projects.vercel.app"
}, allowCredentials = "true")
public class VisitCounterController {
    
    @Autowired
    private VisitCounterService visitCounterService;
    
    /**
     * Increment visit counter - public endpoint
     */
    @PostMapping("/increment")
    public ResponseEntity<Void> incrementVisit() {
        visitCounterService.incrementVisit();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get visit count - public endpoint
     */
    @GetMapping("/count")
    public ResponseEntity<VisitCounter> getVisitCount() {
        VisitCounter counter = visitCounterService.getVisitCount();
        return ResponseEntity.ok(counter);
    }
}
