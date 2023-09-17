package com.vrj.coh.tsp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.vrj.coh.tsp.City.findById;
import static com.vrj.coh.tsp.Connection.findByCity1AndCity2;


@Data
@NoArgsConstructor
public class Solution {

    public final static int MAX_CITIES = 1092;

    /* Adjacency matrix induced by the path of cities. */
    private Double[][] adjacencyMatrix;
    /* Array of cities. */
    private City[] citiesPath;
    /* Sum of the k-1 heaviest edges assuming that there are k cities. */
    private double normalizer;
    /* Cost of the path. */
    private double cost;
    /* Maximum distance between a pair of cities */
    private double maximum;
    /* Indicates if the solution is feasible. */
    private boolean feasible;


    public boolean getFeasible(){
        return this.feasible;
    }

     /* Initializes an object of type solution. */
    public Solution(int[] idsCitiesPath){
        this.citiesPath = toArrayCities(idsCitiesPath);
        this.normalizer = 0;
        this.cost = 0;
        this.maximum = 0;
        this.adjacencyMatrix = fillAdjacencyMatrix();
        this.feasible = false;
        this.costFunction();
    }

    /**
     * Converts an array of city ids to one of cities
     * @param idsArrayCities array of the id's cities.
     * @return array of cities with id in idsArraysCities.
     */
    public City[] toArrayCities(int[] idsArrayCities){
        City[] cities = new City[idsArrayCities.length];

        for(int i = 0; i < idsArrayCities.length; i++){
            cities[i] = findById(idsArrayCities[i]);
        }
        return cities;
    }

    /**
     * Converts an array of cities to one of city ids.
     * @return array of city ids.
     */
    public int[] toArrayInteger(){
        int[] idsCities = new int[citiesPath.length];

        for(int i = 0; i < citiesPath.length; i++){
            idsCities[i] = citiesPath[i].getId();
        }

        return idsCities;
    }

    public List<Double> getDistances(){
        List<Double> distances = new ArrayList<>();

        for(int i = 0; i < citiesPath.length; i++){
            for(int j = 0; j < citiesPath.length; j++){
                Connection connection = findByCity1AndCity2(citiesPath[i], citiesPath[j]);
                if (connection != null)
                    distances.add(connection.getDistance());
            }
        }
        return distances;
    }

    /**
     * Calculate the heaviest route in citiesPath.
     */
    public void calculeteNormalizer(){
        List<Double> distances = getDistances();

        Collections.sort(distances, Collections.reverseOrder());

        for(int i = 0; i < citiesPath.length - 1; i++){
            this.normalizer += distances.get(i);
        }
        System.out.println("Normalizador: " + (new BigDecimal(this.normalizer)).toPlainString());
        calculateMaximum(distances);
    }

    /**
     * Find the longest distance between two cities in citiesPath;
     */
    public void calculateMaximum(List<Double> distances){
        this.maximum = distances.get(0);
        System.out.println("Máximo: " + this.maximum);
    }
    
    /**
     * Calculate the cost of the path.
     */
    public void costFunction(){
        double sum = 0.0;
        int n = citiesPath.length - 1;

        for (int i = 0; i < n; i++) {
            sum += adjacencyMatrix[citiesPath[i].getId()][citiesPath[i + 1].getId()];
        }

        this.cost = sum / normalizer;
    }
    
    public void isFeasible(){

    }
    

    /**
     * Swap two cities in citiesPath.
     */
    public void swap(){
        Random random = new Random();
        int index1 = random.nextInt(citiesPath.length);
        int index2 = random.nextInt(citiesPath.length);

        if(index1 == index2){
            index2 = random.nextInt(citiesPath.length);
        }

        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }
    

    /**
     * Replaces null entries with 0 in the adjacency matrix.
     */
    public void completeAdjacencyMatrix(Double[][] adjacencyMatrix){
        for(int i = 0; i < MAX_CITIES+1; i++){
            for(int j = 0; j < MAX_CITIES+1; j++){
                if(adjacencyMatrix[i][j] == null){
                    adjacencyMatrix[i][j] = 0.0;
                }
            }
        }
    }

    public Solution copy(){
        Solution solution = new Solution();
        solution.setAdjacencyMatrix(this.adjacencyMatrix);
        solution.setCitiesPath(this.citiesPath);
        solution.setNormalizer(this.normalizer);
        solution.setCost(this.cost);
        solution.setMaximum(this.maximum);

        return solution;
    }

    public Double[][] fillAdjacencyMatrix(){
        calculeteNormalizer();
        Double[][] adjacencyMatrix = new Double[MAX_CITIES+1][MAX_CITIES+1];
        for(int i = 0; i < citiesPath.length; i++){
            for(int j = 0; j < citiesPath.length; j++){
                int id1 = citiesPath[i].getId();
                int id2 = citiesPath[j].getId();

                if(i != j){
                    if(findByCity1AndCity2(citiesPath[i], citiesPath[j]) != null){
                        double distance = findByCity1AndCity2(citiesPath[i], citiesPath[j]).getDistance();
                        adjacencyMatrix[id1][id2] = distance;
                        adjacencyMatrix[id2][id1] = distance;
                    } else{
                        double distance = citiesPath[i].extendedNaturalDistance(citiesPath[j], this.maximum);
                        adjacencyMatrix[id1][id2] = distance;
                        adjacencyMatrix[id2][id1] = distance;
                    }

                }
            }
        }
        completeAdjacencyMatrix(adjacencyMatrix);
        return adjacencyMatrix;
    }

    @Override
    public String toString() {
        String citiesPathString = Arrays.toString(citiesPath)
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        BigDecimal costBigDecimal = BigDecimal.valueOf(cost); 
        BigDecimal normalizerBigDecimal = BigDecimal.valueOf(normalizer); 
        String feasibleString = feasible ? "Yes" : "No";

        return "Path: " + citiesPathString + ", Maximun: " + maximum + "Normalizer: " + normalizerBigDecimal.toPlainString() +
               ", Evaluation: " + costBigDecimal.toPlainString() + ", Feasible: " + feasibleString;

    }
}