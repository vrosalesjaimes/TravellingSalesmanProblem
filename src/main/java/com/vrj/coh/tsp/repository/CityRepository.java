package com.vrj.coh.tsp.repository;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findById(Long id);

    List<City> findAll();

    City[] findByIdIn(Long[] ids);

    @Query("SELECT c FROM Connection c WHERE c.city1.id = :cityId OR c.city2.id = :cityId")
    List<Connection> findConnectionsByCityId(Long cityId);
}
