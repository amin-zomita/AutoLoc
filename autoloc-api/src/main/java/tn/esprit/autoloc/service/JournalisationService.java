package tn.esprit.autoloc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JournalisationService {

    public void journaliserCreation(String entite, Long id) {
        log.info("Création : {} id={}", entite, id);
    }

    public void journaliserModification(String entite, Long id) {
        log.info("Modification : {} id={}", entite, id);
    }
}
