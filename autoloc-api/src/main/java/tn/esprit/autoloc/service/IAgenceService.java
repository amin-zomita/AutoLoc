package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public interface IAgenceService {
    List<Agence> findAll();
    Agence findById(Long id);
    Agence create(Agence agence);
    Agence update(Long id, Agence agence);
    void delete(Long id);

    List<Vehicule> findVehicules(Long idAgence);
    List<Employe> findEmployes(Long idAgence);

    void affecterVehicule(Long idAgence, Long idVehicule);
    void desaffecterVehicule(Long idAgence, Long idVehicule);

    void affecterEmploye(Long idAgence, Long idEmploye);
    void desaffecterEmploye(Long idAgence, Long idEmploye);
}