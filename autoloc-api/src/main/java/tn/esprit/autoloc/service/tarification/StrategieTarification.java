package tn.esprit.autoloc.service.tarification;

import java.math.BigDecimal;

public interface StrategieTarification {

    BigDecimal calculer(BigDecimal tarifJournalier, int jours);
}
