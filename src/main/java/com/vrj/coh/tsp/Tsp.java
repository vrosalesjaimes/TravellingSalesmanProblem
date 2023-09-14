package com.vrj.coh.tsp;

import org.springframework.beans.factory.annotation.Autowired;

import com.vrj.coh.tsp.model.City;
import com.vrj.coh.tsp.model.Connection;
import com.vrj.coh.tsp.repository.CityRepository;
import com.vrj.coh.tsp.repository.ConnectionRepository;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    private CityRepository cityRepository;

    private ConnectionRepository connectionRepository;

     /* Initializes an object of type tsp. */
    public Tsp(int[] idsCitiesPath, CityRepository cityRepository, ConnectionRepository connectionRepository){
        this.cityRepository = cityRepository;
        this.connectionRepository = connectionRepository;
        this.citiesPath = toArrayCities(idsCitiesPath);
        this.normalizer = 0;
        this.cost = 0;
        this.solution = null;
        this.maximum = 0;
        this.adjacencyMatrix = fillAdjacencyMatrix();
    }

    public Tsp (CityRepository cityRepository, ConnectionRepository connectionRepository){
        this.cityRepository = cityRepository;
        this.connectionRepository = connectionRepository;
    }

    /**
     * Converts an array of city ids to one of cities
     * @param idsArrayCities array of the id's cities.
     * @return array of cities with id in idsArraysCities.
     */
    public City[] toArrayCities(int[] idsArrayCities){
        City[] cities = new City[idsArrayCities.length];

        for(int i = 0; i < idsArrayCities.length; i++){

            if(cityRepository.findById((long)idsArrayCities[i]).isPresent()){
                cities[i] = cityRepository.findById((long)idsArrayCities[i]).get();
            }
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
            idsCities[i] = citiesPath[i].getId().intValue();
        }

        return idsCities;
    }

    public List<Double> getDistances(){
        List<Double> distances = new ArrayList<>();

        for(int i = 0; i < citiesPath.length; i++){
            for(int j = 0; j < citiesPath.length; j++){
                Optional<Connection> optionalConnection = connectionRepository.findByCity1AndCity2(citiesPath[i], citiesPath[j]);
                Optional<Connection> optionalConnection2 = connectionRepository.findByCity1AndCity2(citiesPath[j], citiesPath[i]);
                
                if(optionalConnection.isPresent()){
                    distances.add(optionalConnection.get().getDistance());
                }

                if(optionalConnection2.isPresent()){
                    distances.add(optionalConnection2.get().getDistance());
                }
            }
        }
        
        return distances;
    }

    /**
     * Calculate the heaviest route in citiesPath.
     */
    public void calculeteNormalizer(){
        List<Double> distances = getDistances();

        Comparator<Double> comparator = Collections.reverseOrder();
        Collections.sort(distances, comparator);

        for(int i = 0; i < citiesPath.length - 1; i++){
            this.normalizer += distances.get(i);
        }
        System.out.println("Normalizador: " + this.normalizer);
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
        for(int i = 0; i < citiesPath.length; i++){
            this.cost += adjacencyMatrix[citiesPath[i].getId().intValue()][citiesPath[i + 1].getId().intValue()]/this.normalizer;
        }
    }

    /**
     * Modify the cost function.
     * @param index1 index of city 1 in citiesPath to exchange
     * @param index2 index of city 2 in citiesPath to exchange
     */
    public void modifyCostFunction(int index1, int index2){
        if(index1 == 0 && index2 == citiesPath.length - 1 ){
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
        Random random = new Random();
        int index1 = random.nextInt(citiesPath.length);
        int index2 = random.nextInt(citiesPath.length);

        City aux = citiesPath[index1];
        citiesPath[index1] = citiesPath[index2];
        citiesPath[index2] = aux;

        if( index1 < index2)
            modifyCostFunction(index1, index2);
        else if(index2 < index1)
            modifyCostFunction(index2, index1);
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

    public Tsp copy(){
        Tsp tsp = new Tsp(this.cityRepository, this.connectionRepository);
        tsp.setAdjacencyMatrix(this.adjacencyMatrix);
        tsp.setCitiesPath(this.citiesPath);
        tsp.setNormalizer(this.normalizer);
        tsp.setCost(this.cost);
        tsp.setSolution(this.solution);
        tsp.setMaximum(this.maximum);

        return tsp;
    }

    public Double[][] fillAdjacencyMatrix(){
        calculeteNormalizer();
        Double[][] adjacencyMatrix = new Double[MAX_CITIES+1][MAX_CITIES+1];
        for(int i = 0; i < citiesPath.length; i++){
            for(int j = 0; j < citiesPath.length; j++){
                int id1 = citiesPath[i].getId().intValue();
                int id2 = citiesPath[j].getId().intValue();

                if(i != j){
                    if(connectionRepository.findByCity1AndCity2(citiesPath[i], citiesPath[j]).isPresent()){
                        double distance = connectionRepository.findByCity1AndCity2(citiesPath[i], citiesPath[j]).get().getDistance();
                        adjacencyMatrix[id1][id2] = distance;
                        adjacencyMatrix[id2][id1] = distance;
                    } else{
                        double distance = citiesPath[i].extendedNaturalDistance(citiesPath[j], this.normalizer);
                        adjacencyMatrix[id1][id2] = distance;
                        adjacencyMatrix[id2][id1] = distance;
                    }

                }
            }
        }
        completeAdjacencyMatrix(adjacencyMatrix);
        return adjacencyMatrix;
    }
}
