package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

public interface IEquipementService {
    List<Equipement> findAll();
    Equipement findById(Long id);
    Equipement create(Equipement equipement);
    Equipement update(Long id, Equipement equipement);
    void delete(Long id);
}