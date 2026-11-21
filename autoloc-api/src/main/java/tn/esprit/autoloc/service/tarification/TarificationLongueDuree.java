package tn.esprit.autoloc.service.tarification;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("LONGUE_DUREE")
public class TarificationLongueDuree implements StrategieTarification {

    private static final BigDecimal COEFFICIENT = new BigDecimal("0.80");

    @Override
    public BigDecimal calculer(BigDecimal tarifJournalier, int jours) {
        return tarifJournalier
                .multiply(BigDecimal.valueOf(jours))
                .multiply(COEFFICIENT)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
