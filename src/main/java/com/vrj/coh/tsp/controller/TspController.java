package com.vrj.coh.tsp.controller;

import com.vrj.coh.tsp.Tsp;
import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.repository.CityRepository;
import com.vrj.coh.tsp.service.ThresholdAcceptingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("tsp/")
@CrossOrigin(origins = "*")
public class TspController {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    private ThresholdAcceptingService thresholdAcceptingService;

    @GetMapping("city/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id){
        Optional<City> optionalCity = this.cityRepository.findById(id);

        City city = null;

        if(optionalCity.isPresent())
            city = optionalCity.get();

        return ResponseEntity.ok(city);
    }

    @GetMapping("/tsp/calcular-solucion")
public ResponseEntity<?> calcularSolucion(
        @RequestParam("idCitiesPath") String idCitiesPathStr,
        @RequestParam("semilla") int semilla,
        @RequestParam("temperaturaInicial") int temperaturaInicial,
        @RequestParam("epsilon") double epsilon,
        @RequestParam("epsilonP") double epsilonP,
        @RequestParam("phi") double phi) {
    
    // Analizar la cadena de entrada en un arreglo de enteros
    int[] idCitiesPath = Arrays.stream(idCitiesPathStr.split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray();
    
    return ResponseEntity.ok(thresholdAcceptingService.main(idCitiesPath, semilla, temperaturaInicial, epsilon, epsilonP, phi));
}


}
