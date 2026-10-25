package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Maintenance introuvable : " + id));
    }

    @Override
    @Transactional
    public Maintenance create(Maintenance maintenance) {
        if (maintenance.getVehicule() == null || maintenance.getVehicule().getIdVehicule() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le véhicule est obligatoire");
        }
        Vehicule vehicule = vehiculeRepository.findById(maintenance.getVehicule().getIdVehicule())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        vehicule.setStatut(StatutVehicule.MAINTENANCE);
        vehiculeRepository.save(vehicule);

        maintenance.setVehicule(vehicule);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        maintenanceRepository.deleteById(id);
    }
}