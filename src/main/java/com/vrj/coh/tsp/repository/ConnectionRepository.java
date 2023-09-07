package com.vrj.coh.tsp.repository;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByCity1AndCity2(City city1, City city2);

}