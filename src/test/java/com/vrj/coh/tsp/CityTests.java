package com.vrj.coh.tsp;

import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

public class CityTests {

    @Rule public Timeout expiracion = Timeout.seconds(5);

    private static Random random;

    @BeforeClass
    public static void initialize() {
        random = new Random();
    }

    private City city;

    @Before
    public void setUp() {
        String name = generateRandomName();
        String country = "Country " + random.nextInt(100);
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
        String newName = "New Name";
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

    private String generateRandomName() {
        int n = random.nextInt(NAMES.length);
        int ap = random.nextInt(LAST_NAMES.length);
        int am = random.nextInt(LAST_NAMES.length);
        return NAMES[n] + " " + LAST_NAMES[ap] + " " + LAST_NAMES[am];
    }

    private static final String[] NAMES = {
            "José Arcadio", "Úrsula", "Aureliano", "Amaranta", "Rebeca",
            "Remedios", "Aureliano José", "Gerinaldo", "Mauricio", "Petra"
    };

    private static final String[] LAST_NAMES = {
            "Buendía", "Iguarán", "Cotes", "Ternera", "Moscote",
            "Brown", "Carpio", "Piedad", "Crespi", "Babilonia"
    };
}
