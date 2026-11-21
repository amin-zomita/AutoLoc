package tn.esprit.autoloc.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Component
@RequiredArgsConstructor
public class VehiculeValidator {

    private final IVehiculeRepository vehiculeRepository;

    // Création : aucun véhicule ne doit déjà avoir cette immatriculation
    public void validerCreation(Vehicule vehicule) {
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw conflitImmatriculation(vehicule.getImmatriculation());
        }
    }

    // Modification : même contrôle, en excluant le véhicule modifié lui-même
    public void validerModification(Long idVehicule, Vehicule vehicule) {
        if (vehiculeRepository.existsByImmatriculationAndIdVehiculeNot(
                vehicule.getImmatriculation(), idVehicule)) {
            throw conflitImmatriculation(vehicule.getImmatriculation());
        }
    }

    private ResponseStatusException conflitImmatriculation(String immatriculation) {
        return new ResponseStatusException(HttpStatus.CONFLICT,
                "Immatriculation déjà utilisée par un autre véhicule : " + immatriculation);
    }
}
