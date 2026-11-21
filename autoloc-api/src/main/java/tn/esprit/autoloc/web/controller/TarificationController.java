package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.tarification.TarificationService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/tarification")
@RequiredArgsConstructor
public class TarificationController {

    private final TarificationService tarificationService;

    @GetMapping
    public BigDecimal calculerPrix(@RequestParam Long idVehicule,
                                   @RequestParam String type,
                                   @RequestParam int jours) {
        return tarificationService.calculerPrix(idVehicule, type, jours);
    }
}
