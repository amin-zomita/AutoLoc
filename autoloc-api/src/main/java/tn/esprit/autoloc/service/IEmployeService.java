package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Employe;

import java.util.List;

public interface IEmployeService {
    List<Employe> findAll();
    Employe findById(Long id);
    Employe create(Employe employe);
    Employe update(Long id, Employe employe);
    void delete(Long id);
}