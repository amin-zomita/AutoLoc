package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;

@Component
public class VehiculeMapper {

    // Requête -> entité : ni id (généré par la base), ni statut (fixé par le service),
    // ni agence (gérée par les endpoints d'affectation)
    public Vehicule toEntity(VehiculeRequestDTO dto) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.immatriculation());
        vehicule.setMarque(dto.marque());
        vehicule.setModele(dto.modele());
        vehicule.setCategorie(dto.categorie());
        vehicule.setTarifJournalier(dto.tarifJournalier());
        return vehicule;
    }

    // Entité -> réponse : l'agence est aplatie en idAgence / nomAgence (null si non affecté)
    public VehiculeResponseDTO toResponse(Vehicule vehicule) {
        Agence agence = vehicule.getAgence();
        return new VehiculeResponseDTO(
                vehicule.getIdVehicule(),
                vehicule.getImmatriculation(),
                vehicule.getMarque(),
                vehicule.getModele(),
                vehicule.getCategorie(),
                vehicule.getTarifJournalier(),
                vehicule.getStatut(),
                agence != null ? agence.getIdAgence() : null,
                agence != null ? agence.getNom() : null);
    }
}
