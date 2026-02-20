package tn.esprit.tpfoyer17.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;

import java.util.List;

@Repository
public interface ChambreRepository extends CrudRepository<Chambre,Long> {

    List<Chambre> findAllByNumeroChambreIn(List<Long> numeroChambreList);
    List<Chambre> findByBlocIdBloc(Long idBloc);

    List<Chambre> findByTypeChambreAndReservationsEstValide(TypeChambre typeChambre , boolean estvalide);
List<Chambre> findByBlocFoyerCapaciteFoyerGreaterThan(long value);

    Chambre findByReservationsIdReservation(String reservations_idReservation) ;

    List<Chambre> findByBlocFoyerUniversiteNomUniversiteLike(String nomUni);
    public List<Chambre> findByBlocIdBlocAndTypeChambre(long idBloc, TypeChambre typeC) ;


}
