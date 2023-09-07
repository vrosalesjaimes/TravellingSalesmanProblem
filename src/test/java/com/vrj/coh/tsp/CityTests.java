package com.vrj.coh.tsp;

import java.util.Optional;
import java.util.Random;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.model.Connection;
import com.vrj.coh.tsp.repository.CityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityTests {

    @Rule public Timeout expiracion = Timeout.seconds(5);

    private static Random random;

    @Autowired
    private CityRepository cityRepository;

    @BeforeClass
    public static void initialize() {
        random = new Random();
    }

    private City city;

    @Before
    public void setUp() {
        String name = generateRandomCityName();
        String country = generateRandomCountry();
        int population = random.nextInt(1000000);
        double latitude = random.nextDouble();
        double longitude = random.nextDouble();
        city = new City(null, name, country, population, latitude, longitude, null);
    }

    @Test
    public void testGetName() {
        String name = city.getName();
        Assert.assertNotNull(name);
    }

    @Test
    public void testSetName() {
        String newName = "New City Name";
        city.setName(newName);
        Assert.assertEquals(newName, city.getName());
    }

    @Test
    public void testGetCountry() {
        String country = city.getCountry();
        Assert.assertNotNull(country);
    }

    @Test
    public void testSetCountry() {
        String newCountry = "New Country";
        city.setCountry(newCountry);
        Assert.assertEquals(newCountry, city.getCountry());
    }

    @Test
    public void testGetPopulation() {
        int population = city.getPopulation();
        Assert.assertTrue(population >= 0);
    }

    @Test
    public void testSetPopulation() {
        int newPopulation = 1000000;
        city.setPopulation(newPopulation);
        Assert.assertEquals(newPopulation, city.getPopulation());
    }

    @Test
    public void testGetLatitude() {
        double latitude = city.getLatitude();
        Assert.assertTrue(latitude >= 0.0 && latitude <= 1.0);
    }

    @Test
    public void testSetLatitude() {
        double newLatitude = 0.75;
        city.setLatitude(newLatitude);
        Assert.assertEquals(newLatitude, city.getLatitude(), 0.0001);
    }

    @Test
    public void testGetLongitude() {
        double longitude = city.getLongitude();
        Assert.assertTrue(longitude >= 0.0 && longitude <= 1.0);
    }

    @Test
    public void testSetLongitude() {
        double newLongitude = 0.25;
        city.setLongitude(newLongitude);
        Assert.assertEquals(newLongitude, city.getLongitude(), 0.0001);
    }

    @Test
    public void testCalculateNaturalDistance(){
        long idCity = random.nextLong(1092) + 1;
        Optional<City> city = cityRepository.findById(idCity);

        City cityTest = null;

        if (city.isPresent())
            cityTest = city.get();

        boolean isCorrectDistance = false;

        for (Connection c: cityTest.getConnections()){
            double  MAX_ERROR_EXPECTED = 0.99;
            double difference = Math.abs(c.getCity1().calculateNaturalDistance(c.getCity2()) - c.getDistance());

            if (difference < MAX_ERROR_EXPECTED)
                isCorrectDistance = true;
        }

        Assert.assertTrue(isCorrectDistance);
    }

    private String generateRandomCityName() {
        int n = random.nextInt(CITY_NAMES.length);
        return CITY_NAMES[n];
    }

    private String generateRandomCountry() {
        int c = random.nextInt(COUNTRIES.length);
        return COUNTRIES[c];
    }

    private static final String[] CITY_NAMES = {
            "New York", "Tokyo", "Paris", "London", "Berlin",
            "Sydney", "Rome", "Beijing", "Moscow", "Rio de Janeiro"
    };

    private static final String[] COUNTRIES = {
            "United States", "Japan", "France", "United Kingdom", "Germany",
            "Australia", "Italy", "China", "Russia", "Brazil"
    };
}