package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule create(Vehicule vehicule);

    Vehicule update(Long id, Vehicule vehicule);

    void delete(Long id);
}