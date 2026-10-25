package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

public interface IReservationService {
    List<Reservation> findAll();
    Reservation findById(Long id);
    Reservation create(Reservation reservation);
    Reservation annuler(Long id);
    void delete(Long id);
}