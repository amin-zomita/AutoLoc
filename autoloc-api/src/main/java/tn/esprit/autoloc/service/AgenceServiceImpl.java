package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agence introuvable : " + id));
    }

    @Override
    public Agence create(Agence agence) {
        return agenceRepository.save(agence);
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existante = findById(id);
        existante.setNom(agence.getNom());
        existante.setVille(agence.getVille());
        existante.setAdresse(agence.getAdresse());
        existante.setTelephone(agence.getTelephone());
        return agenceRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        agenceRepository.deleteById(id);
    }
}