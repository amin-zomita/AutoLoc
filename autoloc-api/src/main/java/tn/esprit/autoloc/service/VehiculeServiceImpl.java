package tn.esprit.autoloc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.validation.VehiculeValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;
    private final VehiculeValidator vehiculeValidator;
    private final JournalisationService journalisationService;

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule introuvable : " + id));
    }

    @Override
    public Vehicule create(Vehicule vehicule) {
        vehiculeValidator.valider(vehicule);
        Vehicule cree = vehiculeRepository.save(vehicule);
        journalisationService.journaliserCreation("Vehicule", cree.getIdVehicule());
        return cree;
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        vehiculeValidator.valider(vehicule);
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(vehicule.getAgence());
        Vehicule modifie = vehiculeRepository.save(existant);
        journalisationService.journaliserModification("Vehicule", modifie.getIdVehicule());
        return modifie;
    }

    @Override
    public void delete(Long id) {
        findById(id);
        vehiculeRepository.deleteById(id);
    }

    @Override
    public List<Equipement> findEquipements(Long idVehicule) {
        return findById(idVehicule).getEquipements();
    }

    @Override
    @Transactional
    public void affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Équipement introuvable : " + idEquipement));
        if (vehicule.getEquipements().contains(equipement)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Équipement déjà affecté à ce véhicule");
        }
        vehicule.addEquipement(equipement);
    }

    @Override
    @Transactional
    public void desaffecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Équipement introuvable : " + idEquipement));
        if (!vehicule.getEquipements().contains(equipement)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Équipement non affecté à ce véhicule");
        }
        vehicule.removeEquipement(equipement);
    }
}