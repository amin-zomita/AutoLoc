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

import java.time.LocalDate;
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
        if (maintenance.getDateFin() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date de fin ne doit pas être fournie : elle est renseignée en terminant la maintenance");
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
    @Transactional
    public Maintenance terminer(Long id) {
        Maintenance maintenance = findById(id);
        if (maintenance.getDateFin() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cette maintenance est déjà terminée");
        }
        LocalDate aujourdhui = LocalDate.now();
        if (maintenance.getDateDebut().isAfter(aujourdhui)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La maintenance n'a pas encore commencé");
        }
        maintenance.setDateFin(aujourdhui);
        mettreAJourStatutVehicule(maintenance);
        return maintenance;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Maintenance maintenance = findById(id);
        if (maintenance.getDateFin() == null) {
            mettreAJourStatutVehicule(maintenance);
        }
        maintenanceRepository.deleteById(id);
    }

    private void mettreAJourStatutVehicule(Maintenance maintenance) {
        Vehicule vehicule = maintenance.getVehicule();
        if (vehicule == null || vehicule.getStatut() != StatutVehicule.MAINTENANCE) {
            return;
        }
        boolean autreMaintenanceEnCours = maintenanceRepository
                .existsByVehiculeIdVehiculeAndDateFinIsNullAndIdMaintenanceNot(
                        vehicule.getIdVehicule(), maintenance.getIdMaintenance());
        if (!autreMaintenanceEnCours) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
    }
}