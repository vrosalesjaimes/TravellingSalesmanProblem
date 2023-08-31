package com.vrj.coh.tsp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    /* The id of the city. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* The name of the city. */
    private String name;
    /* Country where the city is located. */
    private String country;
    /* Population of the city. */
    private int population;
    /* Latitude of the city. */
    private double latitude;
    /* Longitude of the city. */
    private double longitude;

    @OneToMany(mappedBy = "city1", cascade = CascadeType.ALL)
    private Set<Connection> connectionsFrom = new HashSet<>();

    @OneToMany(mappedBy = "city2", cascade = CascadeType.ALL)
    private Set<Connection> connectionsTo = new HashSet<>();

}
