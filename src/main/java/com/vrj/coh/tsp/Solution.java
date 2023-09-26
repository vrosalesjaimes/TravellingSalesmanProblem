package com.vrj.coh.tsp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.vrj.coh.tsp.City.findById;
import static com.vrj.coh.tsp.Connection.findByCity1AndCity2;

/**
 * This class represents a solution for the Traveling Salesman Problem (TSP).
 */
@Getter
@Setter
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
    private Cost cost;
    /* Maximum distance between a pair of cities */
    private double maximum;
    /* Indicates if the solution is feasible. */
    private boolean feasible;

    private static Random random;
    private int index1 = 0;
    private int index2 = 0;

    /**
     * Initializes an object of type Solution.
     * 
     * @param idsCitiesPath An array of city IDs representing the path.
     * @param seed The random seed for generating solutions.
     */
    public Solution(int[] idsCitiesPath, long seed){
        this.citiesPath = toArrayCities(idsCitiesPath);
        this.normalizer = 0;
        this.cost = null;
        this.maximum = 0;
        this.adjacencyMatrix = fillAdjacencyMatrix();
        this.feasible = false;
        random = new Random(seed);
        costFunction();
    }

    /**
     * Converts an array of city IDs to an array of City objects.
     * 
     * @param idsArrayCities An array of city IDs.
     * @return An array of City objects.
     */
    public City[] toArrayCities(int[] idsArrayCities){
        City[] cities = new City[idsArrayCities.length];

        for(int i = 0; i < idsArrayCities.length; i++){
            cities[i] = findById(idsArrayCities[i]);
        }
        return cities;
    }

    /**
     * Converts the array of cities to an array of city IDs.
     * 
     * @return An array of city IDs.
     */
    public int[] toArrayInteger(){
        int[] idsCities = new int[citiesPath.length];

        for(int i = 0; i < citiesPath.length; i++){
            idsCities[i] = citiesPath[i].getId();
        }

        return idsCities;
    }

    /**
     * Gets a list of distances between cities in the path.
     * 
     * @return A list of distances.
     */
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
    public void calculateNormalizer(){
        List<Double> distances = getDistances();

        Collections.sort(distances, Collections.reverseOrder());

        for(int i = 0; i < citiesPath.length - 1; i++){
            this.normalizer += distances.get(i);
        }
        calculateMaximum(distances);
    }

    /**
     * Find the longest distance between two cities in citiesPath.
     */
    public void calculateMaximum(List<Double> distances){
        this.maximum = distances.get(0);
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

        this.cost = new Cost(sum, this.normalizer);
        this.isFeasible();
    }
    
    /**
     * Checks if the solution is feasible.
     */
    public void isFeasible(){
        if(this.cost.getCost() < 1.0 )
            this.feasible = true;
        else
            this.feasible = false;
    }

    /**
     * Swap two cities in citiesPath.
     */
    public void swap(){
        index1 = random.nextInt(citiesPath.length);
        index2 = random.nextInt(citiesPath.length);

        while (index1 == index2){
            index2 = random.nextInt(citiesPath.length);
        }

        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }

    /**
     * Swap two cities in citiesPath.
     */
    public void swap(int index1, int index2){
        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }

    /**
     * Undo the swap operation.
     */
    public void unSwap(){
        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }

    public boolean swept(){
        boolean thereIsLess = false;
        for(int i = 0; i < citiesPath.length; i++){
            for (int j = i+1; j < citiesPath.length; j++){
                double bestCost = this.cost.getCost();
                this.swap(i, j);
                if(bestCost <= this.getCost().getCost()){
                    this.swap(i, j);
                } else{
                    thereIsLess = true;
                    return thereIsLess;
                }
            }
        }

        return thereIsLess;
    }

    /**
     * Replaces null entries with 0 in the adjacency matrix.
     * 
     * @param adjacencyMatrix The adjacency matrix to complete.
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

    /**
     * Creates a copy of the current solution.
     * 
     * @return A copy of the current solution.
     */
    public Solution copy(){
        Solution solution = new Solution();
        solution.setAdjacencyMatrix(this.adjacencyMatrix);
        solution.setCitiesPath(this.citiesPath.clone());
        solution.setNormalizer(this.normalizer);
        solution.setCost(this.cost);
        solution.setMaximum(this.maximum);
        solution.setFeasible(feasible);

        return solution;
    }

    /**
     * Fills the adjacency matrix with distances between cities in the path.
     * 
     * @return The filled adjacency matrix.
     */
    public Double[][] fillAdjacencyMatrix(){
        calculateNormalizer();
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
                    } 
                    else if(findByCity1AndCity2(citiesPath[j], citiesPath[i]) != null){
                        double distance = findByCity1AndCity2(citiesPath[j], citiesPath[i]).getDistance();
                        adjacencyMatrix[id1][id2] = distance;
                        adjacencyMatrix[id2][id1] = distance;
                    }else{
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
        String citiesPathString = Arrays.toString(toArrayInteger())
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");

        double costdouble = cost.getCost(); 
        String feasibleString = feasible ? "YES" : "NO";

        return "Path: " + citiesPathString + "\nMaximun: " + maximum + "\nNormalizer: " + normalizer +
               "\nEvaluation: " + costdouble + "\nFeasible: " + feasibleString;

    }
}
