package tn.esprit.autoloc.service.tarification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TarificationService {

    private final Map<String, StrategieTarification> strategies;
    private final IVehiculeService vehiculeService;

    public BigDecimal calculerPrix(Long idVehicule, String type, int jours) {
        if (jours < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le nombre de jours doit être au moins 1");
        }
        if (type == null || type.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le type de tarif est obligatoire");
        }

        String cle = type.trim().toUpperCase(Locale.ROOT);
        StrategieTarification strategie = strategies.get(cle);
        if (strategie == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Type de tarif inconnu : " + type + ". Types disponibles : " + strategies.keySet());
        }

        Vehicule vehicule = vehiculeService.findById(idVehicule);
        return strategie.calculer(vehicule.getTarifJournalier(), jours);
    }
}
