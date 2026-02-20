package tn.esprit.tpfoyer17.services.interfaces;


import tn.esprit.tpfoyer17.entities.Reservation;

import java.util.List;

public interface IReservationService {
    List<Reservation> retrieveAllReservation();
    Reservation updateReservation (Reservation res);
    Reservation retrieveReservation (String idReservation);
    public Reservation ajouterReservation (long idChambre, long cinEtudiant) ;
    public Reservation annulerReservation (long cinEtudiant) ;
}
