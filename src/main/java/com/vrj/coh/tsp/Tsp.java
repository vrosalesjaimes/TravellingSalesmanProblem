package com.vrj.coh.tsp;

import com.vrj.coh.tsp.model.City;

public class Tsp {
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

     /* Initializes an object of type tsp. */
    public Tsp(int[] idsCitiesPath){

    }

    /**
     * Converts an array of city ids to one of cities
     * @param idsArrayCities array of the id's cities.
     * @return array of cities with id in idsArraysCities.
     */
    public City[] toArrayCities(Integer idsArrayCities){
        return null;
    }

    /**
     * Converts an array of cities to one of city ids.
     * @param citiesArray array of cities.
     * @return array of city ids.
     */
    public int[] toArrayInteger(){
        return null;
    }

    /**
     * Calculate the heaviest route in citiesPath.
     */
    public void calculeteNormalizer(){

    }

    /**
     * Find the longest distance between two cities in citiesPath;
     */
    public void calculateMaximum(){

    }
    
    /**
     * Calculate the cost of the path.
     */
    public void costFunction(){

    }

    /**
     * Modify the cost function.
     * @param index1 index of city 1 in citiesPath to exchange
     * @param index2 index of city 2 in citiesPath to exchange
     */
    public void modifyCostFunction(int index1, int index2){

    }

    /**
     * Swap two cities in citiesPath.
     */
    public void swap(){

    }

    /**
     * Replaces null entries with 0 in the adjacency matrix.
     */
    public void completeAdjacencyMatrix(){

    }

}
