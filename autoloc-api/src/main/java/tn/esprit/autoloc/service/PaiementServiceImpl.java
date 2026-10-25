package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final IContratRepository contratRepository;

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paiement introuvable : " + id));
    }

    @Override
    public Paiement create(Paiement paiement) {
        if (paiement.getContrat() == null || paiement.getContrat().getIdContrat() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le contrat est obligatoire");
        }
        Contrat contrat = contratRepository.findById(paiement.getContrat().getIdContrat())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrat introuvable"));
        paiement.setContrat(contrat);
        return paiementRepository.save(paiement);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        paiementRepository.deleteById(id);
    }
}