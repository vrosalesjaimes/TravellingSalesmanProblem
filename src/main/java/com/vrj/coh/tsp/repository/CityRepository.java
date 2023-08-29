package com.vrj.coh.tsp.repository;

import com.vrj.coh.tsp.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(Long id);

    List<City> findAll();

    City[] findByIdIn(Long[] ids);
}
