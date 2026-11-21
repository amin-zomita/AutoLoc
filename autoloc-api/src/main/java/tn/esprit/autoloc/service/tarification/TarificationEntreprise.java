package tn.esprit.autoloc.service.tarification;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("ENTREPRISE")
public class TarificationEntreprise implements StrategieTarification {

    private static final BigDecimal COEFFICIENT = new BigDecimal("0.85");

    @Override
    public BigDecimal calculer(BigDecimal tarifJournalier, int jours) {
        return tarifJournalier
                .multiply(BigDecimal.valueOf(jours))
                .multiply(COEFFICIENT)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
