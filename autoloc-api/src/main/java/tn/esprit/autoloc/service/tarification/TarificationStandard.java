package tn.esprit.autoloc.service.tarification;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("STANDARD")
public class TarificationStandard implements StrategieTarification {

    @Override
    public BigDecimal calculer(BigDecimal tarifJournalier, int jours) {
        return tarifJournalier
                .multiply(BigDecimal.valueOf(jours))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
