package com.vrj.coh.tsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThresholdAccepting {

    private static Integer SEMILLA = null; 
    private final static int TEMPERATURA_INICIAL = 1;
    private final static double EPSILONP = 0.1;
    private final static double EPSILON = 0.0001;
    private final static double PHI = 0.9;
    private final static double P = 0.85;
    private static Solution bestSolution = null;

    public static int[] permutarArreglo(int[] arreglo) {
        int n = arreglo.length;
        int[] permutacion = arreglo.clone();
        Random rand = new Random(SEMILLA);

        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = permutacion[i];
            permutacion[i] = permutacion[j];
            permutacion[j] = temp;
        }

        return permutacion;
    }

    public static LoteResponse calculaLote(double temperature, Solution solution){

        int c = 0;
        double r = 0.0;
        int L = 2000;
        int limit = 0;
        double anteriorCosto = 0;

        while (c < L){

            anteriorCosto = solution.getCost().getCost();
            solution.swap();

            if(solution.getCost().getCost() < anteriorCosto + temperature ){
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

    public static Solution aceptacionPorUmbrales(double temperature, Solution solution){
        double p = 0.0;

        LoteResponse minSolution = null;
        bestSolution = solution.copy();
        int i = 0;

        String costo = String.format("%20.20f", solution.getCost().getCost());
        String temperatura = String.format("%20.20f", (new BigDecimal(temperature)));
        //System.out.println(String.format("Lote: %4d      Cost: %40s      Temperature: %40s     Feasible: %b", 
        //i, costo, temperatura, solution.getFeasible()));

        while (temperature > EPSILON){

            double q = Double.MAX_VALUE;
            while (p <= q){
                q = p;
                minSolution = calculaLote(temperature, solution);
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
            i++;
            costo = String.format("%20.20f", solution.getCost().getCost());
            temperatura = String.format("%20.20f", (new BigDecimal(temperature)));
            //System.out.println(String.format("Lote: %4d      Cost: %40s      Temperature: %40s     Feasible: %b", 
            //i, costo, temperatura, solution.getFeasible()));
        }
        return bestSolution;
    }

    public static double temperaturaInicial(Solution solution, double T, double P){
        double p = porcentajeAceptados(solution, T);
        double T1, T2 = 0;


        if (Math.abs(P - p) < EPSILONP){
            return T;
        }

        if (p < P){
            while (p < P){
                T = 2 * T;
                p = porcentajeAceptados(solution, T);
            }
            T1 = T/2;
            T2 = T;
        } else {
            while (p > P){
                T = T/2;
                p = porcentajeAceptados(solution, T);
            }
            T1 = T;
            T2 = 2 * T;
        }


        return busquedaBinaria(solution, T1, T2, P);
    }

    public static double porcentajeAceptados(Solution solution, double temperature){
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

    public static double busquedaBinaria(Solution solution, double t1, double t2, double P){
        double t = (t1 + t2)/2;
        
        if (t2 - t1 < EPSILONP){
            return t;
        }
        
        double p = porcentajeAceptados(solution, t);

        if (Math.abs(P - p) < EPSILONP){
            return t;
        }
        if (p > P)
            return busquedaBinaria(solution, t1, t, P);
        else
            return busquedaBinaria(solution, t, t2, P);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java TuClase <nombre_archivo> <semilla>");
         return;
        }

            String archivo = args[0];
            SEMILLA = Integer.valueOf(args[1]);

        try {
            List<Integer> idCitiesList = leerArchivo(archivo);
            int[] idCitiesPath = new int[idCitiesList.size()];
            for (int i = 0; i < idCitiesList.size(); i++) {
                idCitiesPath[i] = idCitiesList.get(i);
            }

            long startTime = System.currentTimeMillis();
            int[] permutacion = permutarArreglo(idCitiesPath);
            Solution tsp = new Solution(permutacion, SEMILLA);

            double T = temperaturaInicial(tsp, TEMPERATURA_INICIAL, P);

           // System.out.println("Temperatura inicial calculada: " + (new BigDecimal(T)).toPlainString());
            //System.out.println("------------------------------ Inicia heuristica -----------------------");
            tsp = aceptacionPorUmbrales(T, tsp);

            String tspString = tsp.toString();

            System.out.println(SEMILLA + "," + tsp.getCost().getCost());

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            double elapsedTimeInMinutes = ((double) elapsedTime / (1000 * 60)) * 60;

           // System.out.println("Tiempo transcurrido: " + Math.ceil(elapsedTimeInMinutes) + " segundos");

            //String outputFileName = "solution/solution-" + tsp.getCitiesPath().length + ".tsp";
            //try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            //    writer.write(tspString);
            //}

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static List<Integer> leerArchivo(String archivo) throws IOException {
        List<Integer> idCitiesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] elementos = linea.split(",");
                for (String elemento : elementos) {
                    idCitiesList.add(Integer.parseInt(elemento.trim()));
                }
            }
        }
        return idCitiesList;
    }
}