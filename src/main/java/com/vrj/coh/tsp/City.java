package com.vrj.coh.tsp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
