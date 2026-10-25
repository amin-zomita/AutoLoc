package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable : " + id));
    }

    @Override
    @Transactional
    public Reservation create(Reservation reservation) {
        if (reservation.getClient() == null || reservation.getClient().getIdClient() == null
                || reservation.getVehicule() == null || reservation.getVehicule().getIdVehicule() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le client et le véhicule sont obligatoires");
        }
        if (reservation.getDateDebut() == null || reservation.getDateFin() == null
                || reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dates invalides : la date de fin doit être après la date de début");
        }

        Client client = clientRepository.findById(reservation.getClient().getIdClient())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client introuvable"));
        Vehicule vehicule = vehiculeRepository.findById(reservation.getVehicule().getIdVehicule())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Véhicule introuvable"));

        if (vehicule.getStatut() == StatutVehicule.MAINTENANCE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le véhicule est en maintenance");
        }
        if (reservationRepository.existsChevauchement(vehicule.getIdVehicule(),
                reservation.getDateDebut(), reservation.getDateFin(), StatutReservation.ANNULEE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le véhicule est déjà réservé sur cette période");
        }

        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation annuler(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getStatut() == StatutReservation.TERMINEE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Une réservation terminée ne peut pas être annulée");
        }
        reservation.setStatut(StatutReservation.ANNULEE);
        return reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        reservationRepository.deleteById(id);
    }
}