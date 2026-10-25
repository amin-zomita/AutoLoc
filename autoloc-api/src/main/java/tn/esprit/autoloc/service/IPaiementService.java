package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {
    List<Paiement> findAll();
    Paiement findById(Long id);
    Paiement create(Paiement paiement);
    void delete(Long id);
}