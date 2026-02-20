package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.services.interfaces.IReservationService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("api/reservations")
public class ReservationController {
    @GetMapping("/retrieveAllReservation")
    public List<Reservation> retrieveAllReservation() {
        return reservationService.retrieveAllReservation();
    }
    @PutMapping("/updateReservation")

    public Reservation updateReservation(@RequestBody Reservation res) {
        return reservationService.updateReservation(res);
    }
 @GetMapping("/retrieveReservation/{idReservation}")
    public Reservation retrieveReservation(@PathVariable("idReservation") String idReservation) {
        return reservationService.retrieveReservation(idReservation);
    }
@PostMapping("/ajouterReservation/{idChambre}/{cinEtudiant}")
    public Reservation ajouterReservation( @PathVariable("idChambre") long idChambre,
                                           @PathVariable("cinEtudiant") long cinEtudiant) {
        return reservationService.ajouterReservation(idChambre, cinEtudiant);
    }
@PutMapping("/annulerReservation/{cinEtudiant}")
    public Reservation annulerReservation(@PathVariable("cinEtudiant") long cinEtudiant) {
        return reservationService.annulerReservation(cinEtudiant);
    }

    IReservationService reservationService;
}
