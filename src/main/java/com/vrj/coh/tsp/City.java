package com.vrj.coh.tsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.ResultSet;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    /* The id of the city. */
    private int id;
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

    private static ConnectorDB CONNECTORDB = new ConnectorDB();   

    public double calculateNaturalDistance(City city){
        final int RATIO = 6373000;

        double latitudeRadians = this.latitude * Math.PI / 180;
        double longitudeRadians = this.longitude * Math.PI / 180;
        double cityLatitudeRadians = city.latitude * Math.PI / 180;
        double cityLongitudeRadians = city.longitude * Math.PI / 180;

        double senLatitude = Math.pow(Math.sin((latitudeRadians -  cityLatitudeRadians)/2), 2);
        double senLongitude = Math.pow(Math.sin((longitudeRadians - cityLongitudeRadians)/2), 2);

        double cosLatitude = Math.cos(latitudeRadians);
        double cosCityLatitude = Math.cos(cityLatitudeRadians);

        double A = senLatitude + (cosLatitude*cosCityLatitude*senLongitude);

        double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1-A));

        return RATIO * C;
    }

    public double extendedNaturalDistance(City city, double maximum){
        return this.calculateNaturalDistance(city) * maximum;
    }

    public static City findById(int id){
        String query = "SELECT * FROM cities WHERE id = " + id + ";"; 
        ResultSet resultSet = CONNECTORDB.executeQuery(query);
        
        try {
            if (resultSet.next()) {
                City city = new City();
                city.setId(resultSet.getInt("id"));
                city.setName(resultSet.getString("name"));
                city.setCountry(resultSet.getString("country"));
                city.setPopulation(resultSet.getInt("population"));
                city.setLatitude(resultSet.getDouble("latitude"));
                city.setLongitude(resultSet.getDouble("longitude"));
                return city;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar una ciudad por ID en la tabla cities", e);
        }
    }
}