package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceRepository agenceRepository;

    @GetMapping
    public List<Agence> getAll() {
        return agenceRepository.findAll();
    }

    @PostMapping
    public Agence create(@RequestBody Agence agence) {
        return agenceRepository.save(agence);
    }
}