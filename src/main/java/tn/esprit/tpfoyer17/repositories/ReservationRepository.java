package tn.esprit.tpfoyer17.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer17.entities.Reservation;

import java.time.LocalDate;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation,String> {
    Reservation findByIdReservation(String id);


}
