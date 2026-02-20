package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.services.interfaces.IChambreService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("api/chambres")
public class ChambreController {
    @GetMapping("/retrieveAllChambres")
    public List<Chambre> retrieveAllChambres() {
        return chambreService.retrieveAllChambres();
    }

    @PostMapping("/addChambre")
    public Chambre addChambre(@RequestBody Chambre c) {
        return chambreService.addChambre(c);
    }

    @PutMapping("/updateChambre")
    public Chambre updateChambre(@RequestBody Chambre c) {
        return chambreService.updateChambre(c);
    }

    @GetMapping("/retrieveChambre/{idChambre}")
    public Chambre retrieveChambre(@PathVariable("idChambre") long idChambre) {
        return chambreService.retrieveChambre(idChambre);
    }

    @GetMapping("/findByTypeChambre")
    public List<Chambre> findByTypeChambre() {
        return chambreService.findByTypeChambre();
    }
@PutMapping("/affecterChambresABloc/{idBloc}")
    public Bloc affecterChambresABloc(@RequestBody List<Long> numChambre,
                                      @PathVariable("idBloc") long idBloc) {
        return chambreService.affecterChambresABloc(numChambre, idBloc);
    }
@GetMapping("/getChambresParNomUniversite/{nomUniversite}")
    public List<Chambre> getChambresParNomUniversite(@PathVariable("nomUniversite") String nomUniversite) {
        return chambreService.getChambresParNomUniversite(nomUniversite);
    }

    IChambreService chambreService;
}
