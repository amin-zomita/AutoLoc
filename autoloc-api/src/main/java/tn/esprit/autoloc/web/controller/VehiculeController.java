package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;
    private final VehiculeMapper vehiculeMapper;

    @GetMapping
    public List<VehiculeResponseDTO> getAll() {
        return vehiculeService.findAll().stream()
                .map(vehiculeMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public VehiculeResponseDTO getById(@PathVariable Long id) {
        return vehiculeMapper.toResponse(vehiculeService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeResponseDTO create(@Valid @RequestBody VehiculeRequestDTO dto) {
        Vehicule cree = vehiculeService.create(vehiculeMapper.toEntity(dto));
        return vehiculeMapper.toResponse(cree);
    }

    @PutMapping("/{id}")
    public VehiculeResponseDTO update(@PathVariable Long id,
                                      @Valid @RequestBody VehiculeRequestDTO dto) {
        Vehicule modifie = vehiculeService.update(id, vehiculeMapper.toEntity(dto));
        return vehiculeMapper.toResponse(modifie);
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