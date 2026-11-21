package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agence introuvable : " + id));
    }

    @Override
    public Agence create(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existante = findById(id);
        existante.setNom(agence.getNom());
        existante.setVille(agence.getVille());
        existante.setAdresse(agence.getAdresse());
        existante.setTelephone(agence.getTelephone());
        return agenceRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        agenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findVehicules(Long idAgence) {
        return findById(idAgence).getVehicules();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findEmployes(Long idAgence) {
        return findById(idAgence).getEmployes();
    }

    @Override
    @Transactional
    public void affecterVehicule(Long idAgence, Long idVehicule) {
        Agence agence = findById(idAgence);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule introuvable : " + idVehicule));
        if (vehicule.getAgence() != null) {
            vehicule.getAgence().removeVehicule(vehicule);
        }
        agence.addVehicule(vehicule);
    }

    @Override
    @Transactional
    public void desaffecterVehicule(Long idAgence, Long idVehicule) {
        Agence agence = findById(idAgence);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule introuvable : " + idVehicule));
        if (vehicule.getAgence() == null || !vehicule.getAgence().getIdAgence().equals(idAgence)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ce véhicule n'appartient pas à cette agence");
        }
        agence.removeVehicule(vehicule);
    }

    @Override
    @Transactional
    public void affecterEmploye(Long idAgence, Long idEmploye) {
        Agence agence = findById(idAgence);
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employé introuvable : " + idEmploye));
        if (employe.getAgence() != null) {
            employe.getAgence().removeEmploye(employe);
        }
        agence.addEmploye(employe);
    }

    @Override
    @Transactional
    public void desaffecterEmploye(Long idAgence, Long idEmploye) {
        Agence agence = findById(idAgence);
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employé introuvable : " + idEmploye));
        if (employe.getAgence() == null || !employe.getAgence().getIdAgence().equals(idAgence)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cet employé n'appartient pas à cette agence");
        }
        agence.removeEmploye(employe);
    }
}