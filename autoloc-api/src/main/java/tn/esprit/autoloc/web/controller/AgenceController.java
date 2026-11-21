package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;
    private final VehiculeMapper vehiculeMapper;

    @GetMapping
    public List<Agence> getAll() {
        return agenceService.findAll();
    }

    @GetMapping("/{id}")
    public Agence getById(@PathVariable Long id) {
        return agenceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence create(@RequestBody Agence agence) {
        return agenceService.create(agence);
    }

    @PutMapping("/{id}")
    public Agence update(@PathVariable Long id, @RequestBody Agence agence) {
        return agenceService.update(id, agence);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        agenceService.delete(id);
    }

    @GetMapping("/{id}/vehicules")
    public List<VehiculeResponseDTO> getVehicules(@PathVariable Long id) {
        return agenceService.findVehicules(id).stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}/vehicules/{idVehicule}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterVehicule(@PathVariable Long id, @PathVariable Long idVehicule) {
        agenceService.affecterVehicule(id, idVehicule);
    }

    @DeleteMapping("/{id}/vehicules/{idVehicule}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterVehicule(@PathVariable Long id, @PathVariable Long idVehicule) {
        agenceService.desaffecterVehicule(id, idVehicule);
    }

    @GetMapping("/{id}/employes")
    public List<Employe> getEmployes(@PathVariable Long id) {
        return agenceService.findEmployes(id);
    }

    @PutMapping("/{id}/employes/{idEmploye}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterEmploye(@PathVariable Long id, @PathVariable Long idEmploye) {
        agenceService.affecterEmploye(id, idEmploye);
    }

    @DeleteMapping("/{id}/employes/{idEmploye}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterEmploye(@PathVariable Long id, @PathVariable Long idEmploye) {
        agenceService.desaffecterEmploye(id, idEmploye);
    }
}