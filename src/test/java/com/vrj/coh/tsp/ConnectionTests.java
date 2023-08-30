package com.vrj.coh.tsp;

import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

public class ConnectionTests {

    @Rule public Timeout expiracion = Timeout.seconds(5);

    private static Random random;

    @BeforeClass
    public static void initialize() {
        random = new Random();
    }

    private Connection connection;

    @Before
    public void setUp() {
        City city1 = new City();
        city1.setName(generateRandomCityName());
        city1.setCountry("Country " + random.nextInt(100));
        city1.setPopulation(random.nextInt(1000000));
        city1.setLatitude(random.nextDouble());
        city1.setLongitude(random.nextDouble());

        City city2 = new City();
        city2.setName(generateRandomCityName());
        city2.setCountry("Country " + random.nextInt(100));
        city2.setPopulation(random.nextInt(1000000));
        city2.setLatitude(random.nextDouble());
        city2.setLongitude(random.nextDouble());

        connection = new Connection();
        connection.setCity1(city1);
        connection.setCity2(city2);
        connection.setDistance(random.nextDouble());
    }

    @Test
    public void testGetCity1() {
        City city1 = connection.getCity1();
        Assert.assertNotNull(city1);
    }

    @Test
    public void testSetCity1() {
        City newCity1 = new City();
        newCity1.setName("New City 1");
        newCity1.setCountry("New Country 1");
        newCity1.setPopulation(1000000);
        newCity1.setLatitude(0.5);
        newCity1.setLongitude(0.5);

        connection.setCity1(newCity1);
        Assert.assertEquals(newCity1, connection.getCity1());
    }

    @Test
    public void testGetCity2() {
        City city2 = connection.getCity2();
        Assert.assertNotNull(city2);
    }

    @Test
    public void testSetCity2() {
        City newCity2 = new City();
        newCity2.setName("New City 2");
        newCity2.setCountry("New Country 2");
        newCity2.setPopulation(1000000);
        newCity2.setLatitude(0.5);
        newCity2.setLongitude(0.5);

        connection.setCity2(newCity2);
        Assert.assertEquals(newCity2, connection.getCity2());
    }

    @Test
    public void testGetDistance() {
        double distance = connection.getDistance();
        Assert.assertTrue(distance >= 0.0);
    }

    @Test
    public void testSetDistance() {
        double newDistance = 123.45;
        connection.setDistance(newDistance);
        Assert.assertEquals(newDistance, connection.getDistance(), 0.0001);
    }

    @Test
    public void testCalculateDistance() {
        City city1 = connection.getCity1();
        City city2 = connection.getCity2();
        double calculatedDistance = city1.calculateNaturalDistance(city2);
        Assert.assertTrue(calculatedDistance >= 0.0);
    }

    private String generateRandomCityName() {
        int n = random.nextInt(CITY_NAMES.length);
        return CITY_NAMES[n];
    }

    private static final String[] CITY_NAMES = {
            "New York", "Tokyo", "Paris", "London", "Berlin",
            "Sydney", "Rome", "Beijing", "Moscow", "Rio de Janeiro"
    };
}
