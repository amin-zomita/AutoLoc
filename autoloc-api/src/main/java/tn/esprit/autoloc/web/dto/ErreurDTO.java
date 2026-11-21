package tn.esprit.autoloc.web.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErreurDTO(
        int status,
        String erreur,
        String message,
        LocalDateTime date,
        String chemin,
        List<ChampInvalide> champs
) {
}
