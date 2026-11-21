package tn.esprit.autoloc.service.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;

@Component
public class VehiculeValidator {

    public void valider(Vehicule vehicule) {
        BigDecimal tarif = vehicule.getTarifJournalier();

        if (tarif == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le tarif journalier est obligatoire");
        }
        if (tarif.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le tarif journalier doit être strictement positif");
        }
    }
}
