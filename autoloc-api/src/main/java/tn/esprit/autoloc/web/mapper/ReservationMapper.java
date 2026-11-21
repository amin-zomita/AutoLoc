package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

@Component
public class ReservationMapper {

    // Requête -> entité : client et véhicule ne contiennent que leur id ;
    // le service les remplace par les vrais objets chargés depuis la base
    public Reservation toEntity(ReservationRequestDTO dto) {
        Client client = new Client();
        client.setIdClient(dto.idClient());

        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.idVehicule());

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.dateDebut());
        reservation.setDateFin(dto.dateFin());
        return reservation;
    }

    // Entité -> réponse : client et véhicule aplatis (id + libellé)
    public ReservationResponseDTO toResponse(Reservation reservation) {
        Client client = reservation.getClient();
        Vehicule vehicule = reservation.getVehicule();
        return new ReservationResponseDTO(
                reservation.getIdReservation(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getStatut(),
                client != null ? client.getIdClient() : null,
                client != null ? client.getNom() + " " + client.getPrenom() : null,
                vehicule != null ? vehicule.getIdVehicule() : null,
                vehicule != null ? vehicule.getImmatriculation() : null);
    }
}
