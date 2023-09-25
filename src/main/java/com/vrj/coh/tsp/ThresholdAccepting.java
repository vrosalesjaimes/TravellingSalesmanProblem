package com.vrj.coh.tsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the Threshold Accepting algorithm for solving a Traveling Salesman Problem (TSP).
 */
public class ThresholdAccepting {

    private static Integer SEED = null; 
    private final static int INITIAL_TEMPERATURE = 1;
    private final static double EPSILONP = 0.1;
    private final static double EPSILON = 0.0001;
    private final static double PHI = 0.9;
    private final static double P = 0.85;
    private static Solution bestSolution = null;

    /**
     * Permutes an array randomly.
     * 
     * @param array The array to be permuted.
     * @return The permuted array.
     */
    public static int[] permuteArray(int[] array) {
        int n = array.length;
        int[] permutation = array.clone();
        Random rand = new Random(SEED);

        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }

        return permutation;
    }

    /**
     * Calculates a batch of solutions at a given temperature.
     * 
     * @param temperature The current temperature.
     * @param solution The current solution.
     * @return The batch response.
     */
    public static LoteResponse calculateBatch(double temperature, Solution solution){

        int c = 0;
        double r = 0.0;
        int L = 2000;
        int limit = 0;
        double previousCost = 0;

        while (c < L){

            previousCost = solution.getCost().getCost();
            solution.swap();

            if(solution.getCost().getCost() < previousCost + temperature ){
                c++;
                r += solution.getCost().getCost();
            } else{
                solution.unSwap();
            }

            limit++;
            if (limit > 20000){
                break;
            }
        }
        return new LoteResponse(r/L, solution);
    }

    /**
     * Runs the Threshold Acceptance algorithm.
     * 
     * @param temperature The initial temperature.
     * @param solution The initial solution.
     * @return The best solution found.
     */
    public static Solution thresholdAcceptance(double temperature, Solution solution){
        double p = 0.0;

        LoteResponse minSolution = null;
        bestSolution = solution.copy();

        while (temperature > EPSILON){

            double q = Double.MAX_VALUE;
            while (p <= q){
                q = p;
                minSolution = calculateBatch(temperature, solution);
                if (minSolution.getSolution() != null){
                    p  = minSolution.getPromedio();

                    if(minSolution.getSolution().getCost().getCost() < bestSolution.getCost().getCost()){
                        bestSolution = minSolution.getSolution().copy();
                        bestSolution.isFeasible();
                    }

                    solution = minSolution.getSolution();
                    solution.isFeasible();
                }
            }
            temperature = PHI * temperature;
        }

        while(bestSolution.swept());
        return bestSolution;
    }

    /**
     * Calculates the initial temperature.
     * 
     * @param solution The initial solution.
     * @param T The initial temperature.
     * @param P The target acceptance rate.
     * @return The calculated initial temperature.
     */
    public static double initialTemperature(Solution solution, double T, double P){
        double p = acceptancePercentage(solution, T);
        double T1, T2 = 0;

        if (Math.abs(P - p) < EPSILONP){
            return T;
        }

        if (p < P){
            while (p < P){
                T = 2 * T;
                p = acceptancePercentage(solution, T);
            }
            T1 = T/2;
            T2 = T;
        } else {
            while (p > P){
                T = T/2;
                p = acceptancePercentage(solution, T);
            }
            T1 = T;
            T2 = 2 * T;
        }

        return binarySearch(solution, T1, T2, P);
    }

    /**
     * Calculates the acceptance percentage.
     * 
     * @param solution The solution to evaluate.
     * @param temperature The current temperature.
     * @return The acceptance percentage.
     */
    public static double acceptancePercentage(Solution solution, double temperature){
        int c = 0;
        int N = 10;

        for(int i = 0; i < N; i++){
            solution.swap();

            if(solution.getCost().getCost() < temperature){
                c++;
            } else {
                solution.unSwap();
            }
        }
        return c/N;
    }

    /**
     * Performs a binary search to find the best temperature.
     * 
     * @param solution The solution to evaluate.
     * @param t1 The lower bound of the search range.
     * @param t2 The upper bound of the search range.
     * @param P The target acceptance rate.
     * @return The best temperature.
     */
    public static double binarySearch(Solution solution, double t1, double t2, double P){
        double t = (t1 + t2)/2;
        
        if (t2 - t1 < EPSILONP){
            return t;
        }
        
        double p = acceptancePercentage(solution, t);

        if (Math.abs(P - p) < EPSILONP){
            return t;
        }
        if (p > P)
            return binarySearch(solution, t1, t, P);
        else
            return binarySearch(solution, t, t2, P);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java YourClass <filename> <seed>");
            return;
        }

        String filename = args[0];
        SEED = Integer.valueOf(args[1]);

        try {
            List<Integer> idCitiesList = readFile(filename);
            int[] idCitiesPath = new int[idCitiesList.size()];
            for (int i = 0; i < idCitiesList.size(); i++) {
                idCitiesPath[i] = idCitiesList.get(i);
            }

            int[] permutation = permuteArray(idCitiesPath);
            Solution tsp = new Solution(permutation, SEED);

            double T = initialTemperature(tsp, INITIAL_TEMPERATURE, P);

            tsp = thresholdAcceptance(T, tsp);

            String tspString = tsp.toString();

            System.out.println(tspString);


            String outputFileName = "solution/solution-" + tsp.getCitiesPath().length + ".tsp";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write(tspString);
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static List<Integer> readFile(String filename) throws IOException {
        List<Integer> idCitiesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                for (String element : elements) {
                    idCitiesList.add(Integer.parseInt(element.trim()));
                }
            }
        }
        return idCitiesList;
    }
}
