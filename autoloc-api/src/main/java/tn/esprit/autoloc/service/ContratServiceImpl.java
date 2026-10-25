package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrat introuvable : " + id));
    }

    @Override
    @Transactional
    public Contrat create(Contrat contrat) {
        if (contrat.getReservation() == null || contrat.getReservation().getIdReservation() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La réservation est obligatoire");
        }
        Reservation reservation = reservationRepository
                .findById(contrat.getReservation().getIdReservation())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable"));

        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Impossible de créer un contrat pour une réservation annulée");
        }
        if (contratRepository.existsByReservationIdReservation(reservation.getIdReservation())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cette réservation a déjà un contrat");
        }

        contrat.setReservation(reservation);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservationRepository.save(reservation);
        return contratRepository.save(contrat);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        contratRepository.deleteById(id);
    }
}