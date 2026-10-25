package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            select count(r) > 0 from Reservation r
            where r.vehicule.idVehicule = :idVehicule
              and r.statut <> :annulee
              and r.dateDebut <= :fin
              and r.dateFin >= :debut
            """)
    boolean existsChevauchement(@Param("idVehicule") Long idVehicule,
                                @Param("debut") LocalDate debut,
                                @Param("fin") LocalDate fin,
                                @Param("annulee") StatutReservation annulee);
}