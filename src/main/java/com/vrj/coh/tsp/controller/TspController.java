package com.vrj.coh.tsp.controller;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.repository.CityRepository;
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

    @GetMapping("city/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id){
        Optional<City> optionalCity = this.cityRepository.findById(id);

        City city = null;

        if(optionalCity.isPresent())
            city = optionalCity.get();

        return ResponseEntity.ok(city);
    }


    @GetMapping("tsp/solution")
    public ResponseEntity<?> getSolution(@RequestBody int[] idsCitiesPath){
        return ResponseEntity.ok(null);
    }
}
