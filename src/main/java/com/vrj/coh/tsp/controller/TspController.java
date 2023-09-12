package com.vrj.coh.tsp.controller;

import com.vrj.coh.tsp.Tsp;
import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.repository.CityRepository;
import com.vrj.coh.tsp.service.ThresholdAcceptingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("tsp/")
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


    @GetMapping("tsp/solution")
    public ResponseEntity<String> getSolution(@RequestBody int[] idsCitiesPath, 
                                         @RequestBody int semilla, 
                                         @RequestBody int temperaturaInicial,
                                         @RequestBody double epsilon,
                                         @RequestBody double epesilonP, 
                                         @RequestBody double phi){

        return ResponseEntity.ok(this.thresholdAcceptingService.main(idsCitiesPath, semilla, temperaturaInicial, epsilon, epesilonP, phi));
    }
}
