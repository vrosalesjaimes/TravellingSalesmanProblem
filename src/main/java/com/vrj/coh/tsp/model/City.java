package com.vrj.coh.tsp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public double calculateNaturalDistance(City city){
        final int RATIO = 6373000;

        double latitudeRadians = this.latitude * Math.PI / 180;
        double longitudeRadians = this.longitude * Math.PI / 180;
        double cityLatitudeRadians = this.latitude * Math.PI / 180;
        double cityLongitudeRadians = this.longitude * Math.PI / 180;

        double senLatitude = Math.pow(Math.sin((latitudeRadians -  cityLatitudeRadians)/2), 2);
        double senLongitude = Math.pow(Math.sin((longitudeRadians - cityLongitudeRadians)/2), 2);

        double cosLatitude = Math.cos(latitudeRadians);
        double cosCityLatitude = Math.cos(cityLatitudeRadians);

        double A = senLatitude + (cosLatitude*cosCityLatitude*senLongitude);

        double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1-A));

        return RATIO * C;
    }
}
