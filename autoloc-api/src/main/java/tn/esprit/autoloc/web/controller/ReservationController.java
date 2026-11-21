package tn.esprit.autoloc.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public List<ReservationResponseDTO> getAll() {
        return reservationService.findAll().stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ReservationResponseDTO getById(@PathVariable Long id) {
        return reservationMapper.toResponse(reservationService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDTO create(@Valid @RequestBody ReservationRequestDTO dto) {
        Reservation creee = reservationService.create(reservationMapper.toEntity(dto));
        return reservationMapper.toResponse(creee);
    }

    @PutMapping("/{id}/annuler")
    public ReservationResponseDTO annuler(@PathVariable Long id) {
        return reservationMapper.toResponse(reservationService.annuler(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}