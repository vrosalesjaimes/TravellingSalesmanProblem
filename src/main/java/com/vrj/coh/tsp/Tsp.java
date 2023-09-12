package com.vrj.coh.tsp;

import org.springframework.beans.factory.annotation.Autowired;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.repository.CityRepository;

import lombok.Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Data
public class Tsp {

    public final static int MAX_CITIES = 1092;

    /* Adjacency matrix induced by the path of cities. */
    private Double[][] adjacencyMatrix;
    /* Array of cities. */
    private City[] citiesPath;
    /* Sum of the k-1 heaviest edges assuming that there are k cities. */
    private double normalizer;
    /* Cost of the path. */
    private double cost;
    /* Solution of the TSP. */
    private Solution solution;
    /* Maximum distance between a pair of cities */
    private double maximum;

    @Autowired
    private CityRepository cityRepository;

     /* Initializes an object of type tsp. */
    public Tsp(int[] idsCitiesPath){
        this.adjacencyMatrix = new Double[MAX_CITIES][MAX_CITIES];
        this.citiesPath = toArrayCities(idsCitiesPath);
        this.normalizer = 0;
        this.cost = 0;
        this.solution = null;
        this.maximum = 0;
    }

    /**
     * Converts an array of city ids to one of cities
     * @param idsArrayCities array of the id's cities.
     * @return array of cities with id in idsArraysCities.
     */
    public City[] toArrayCities(int[] idsArrayCities){
        City[] cities = new City[idsArrayCities.length];

        for(int i = 0; i < idsArrayCities.length; i++){
            if(cityRepository.findById((long)idsArrayCities[i]).isPresent());
                cities[i] = cityRepository.findById((long)idsArrayCities[i]).get();
        }

        return cities;
    }

    /**
     * Converts an array of cities to one of city ids.
     * @param citiesArray array of cities.
     * @return array of city ids.
     */
    public int[] toArrayInteger(){
        int[] idsCities = new int[citiesPath.length];

        for(int i = 0; i < citiesPath.length; i++){
            idsCities[i] = citiesPath[i].getId().intValue();
        }

        return idsCities;
    }

    /**
     * Calculate the heaviest route in citiesPath.
     */
    public void calculeteNormalizer(List<Double> distances){
        Comparator<Double> comparator = Collections.reverseOrder();
        Collections.sort(distances, comparator);

        for(int i = 0; i < citiesPath.length - 1; i++){
            this.normalizer += distances.get(i);
        }

        calculateMaximum(distances);
    }

    /**
     * Find the longest distance between two cities in citiesPath;
     */
    public void calculateMaximum(List<Double> distances){
        this.maximum = distances.get(0);
    }
    
    /**
     * Calculate the cost of the path.
     */
    public void costFunction(){
        for(int i = 0; i < citiesPath.length - 1; i++){
            this.cost += adjacencyMatrix[citiesPath[i].getId().intValue()][citiesPath[i + 1].getId().intValue()]/this.normalizer;
        }
    }

    /**
     * Modify the cost function.
     * @param index1 index of city 1 in citiesPath to exchange
     * @param index2 index of city 2 in citiesPath to exchange
     */
    public void modifyCostFunction(int index1, int index2){
        if(index1 == 0 && index2 == citiesPath.length - 1){
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
        }

        if(index1 != 0 && index2 == citiesPath.length - 1){
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 - 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 - 1].getId().intValue()]/this.normalizer;
        }

        if(index1 == 0 && index2 != citiesPath.length-1){
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index2 + 1].getId().intValue()]/this.normalizer;
        }

        if(index1 != 0 && index2 != citiesPath.length - 1){
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index1 - 1].getId().intValue()]/this.normalizer;
            this.cost -= adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index2 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index1 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index2 - 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index1].getId().intValue()][citiesPath[index2 + 1].getId().intValue()]/this.normalizer;
            this.cost += adjacencyMatrix[citiesPath[index2].getId().intValue()][citiesPath[index1 - 1].getId().intValue()]/this.normalizer;
        }
    }

    /**
     * Swap two cities in citiesPath.
     */
    public void swap(){
        Random random = new Random(citiesPath.length);
        int index1 = random.nextInt();
        int index2 = random.nextInt();

        City aux = citiesPath[index1];
        citiesPath[index1] = citiesPath[index2];
        citiesPath[index2] = aux;
    }

    /**
     * Replaces null entries with 0 in the adjacency matrix.
     */
    public void completeAdjacencyMatrix(){
        for(int i = 0; i < citiesPath.length; i++){
            for(int j = 0; j < citiesPath.length; j++){
                if(adjacencyMatrix[i][j] == null){
                    adjacencyMatrix[i][j] = 0.0;
                }
            }
        }
    }

}
