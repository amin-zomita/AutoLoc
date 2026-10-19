package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeRepository vehiculeRepository;

    @GetMapping
    public List<Vehicule> getAll() {
        return vehiculeRepository.findAll();
    }

    @PostMapping
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }
}