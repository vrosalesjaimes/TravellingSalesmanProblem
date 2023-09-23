package com.vrj.coh.tsp;

import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Cost cost;
    /* Maximum distance between a pair of cities */
    private double maximum;
    /* Indicates if the solution is feasible. */
    private boolean feasible;

    private static Random random;
    private int index1 = 0;
    private int index2 = 0;


    public boolean getFeasible(){
        return this.feasible;
    }

     /* Initializes an object of type solution. */
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
        //System.out.println(String.format("Normalizador: %.10f", this.normalizer));
        calculateMaximum(distances);
    }

    /**
     * Find the longest distance between two cities in citiesPath;
     */
    public void calculateMaximum(List<Double> distances){
        this.maximum = distances.get(0);
        //System.out.println(String.format("Máximo: %.10f", this.maximum));
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

    public void unSwap(){
        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }

    public void swapBarrido(int index1, int index2){
        City aux = this.citiesPath[index1];
        this.citiesPath[index1] = this.citiesPath[index2];
        this.citiesPath[index2] = aux;

        this.costFunction();
    }
    
    public double modifyCost(int i, int j) {
        int n = citiesPath.length - 1;
        City[] path = citiesPath;
    
        double newCost = 0.0;
    
        if (Math.abs(i - j) == 1) {
            int ii = Math.min(i, j);
            int jj = Math.max(i, j);
            if (ii != 0) {
                newCost -= adjacencyMatrix[path[ii - 1].getId()][path[ii].getId()];
                newCost += adjacencyMatrix[path[ii - 1].getId()][path[jj].getId()];
            }
            if (jj != n) {
                newCost -= adjacencyMatrix[path[jj].getId()][path[jj + 1].getId()];
                newCost += adjacencyMatrix[path[ii].getId()][path[jj + 1].getId()];
            }
        } else {
            if (i != 0) {
                newCost -= adjacencyMatrix[path[i - 1].getId()][path[i].getId()];
                newCost += adjacencyMatrix[path[i - 1].getId()][path[j].getId()];
            }
            if (i != n) {
                newCost -= adjacencyMatrix[path[i].getId()][path[i + 1].getId()];
                newCost += adjacencyMatrix[path[j].getId()][path[i + 1].getId()];
            }
            if (j != 0) {
                newCost -= adjacencyMatrix[path[j - 1].getId()][path[j].getId()];
                newCost += adjacencyMatrix[path[j - 1].getId()][path[i].getId()];
            }
            if (j != n) {
                newCost -= adjacencyMatrix[path[j].getId()][path[j + 1].getId()];
                newCost += adjacencyMatrix[path[i].getId()][path[j + 1].getId()];
            }
        }
        return newCost;
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
        solution.setCitiesPath(this.citiesPath.clone());
        solution.setNormalizer(this.normalizer);
        solution.setCost(this.cost);
        solution.setMaximum(this.maximum);
        solution.setFeasible(feasible);

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
        String feasibleString = feasible ? "Yes" : "No";

        return "Path: " + citiesPathString + "\nMaximun: " + maximum + "\nNormalizer: " + normalizer +
               "\nEvaluation: " + costdouble + "\nFeasible: " + feasibleString;

    }
}