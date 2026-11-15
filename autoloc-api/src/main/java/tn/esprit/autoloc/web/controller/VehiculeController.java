package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    public List<Vehicule> getAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicule getById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @PutMapping("/{id}")
    public Vehicule update(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculeService.delete(id);
    }

    @GetMapping("/{id}/equipements")
    public List<Equipement> getEquipements(@PathVariable Long id) {
        return vehiculeService.findEquipements(id);
    }

    @PutMapping("/{id}/equipements/{idEquipement}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterEquipement(@PathVariable Long id,
                                   @PathVariable Long idEquipement) {
        vehiculeService.affecterEquipement(id, idEquipement);
    }

    @DeleteMapping("/{id}/equipements/{idEquipement}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterEquipement(@PathVariable Long id,
                                      @PathVariable Long idEquipement) {
        vehiculeService.desaffecterEquipement(id, idEquipement);
    }
}