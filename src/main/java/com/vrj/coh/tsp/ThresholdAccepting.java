package com.vrj.coh.tsp;

import java.math.BigDecimal;
import java.util.Random;

public class ThresholdAccepting {

    private final static int SEMILLA = 1; 
    private final static int TEMPERATURA_INICIAL = 100;
    private final static double EPSILON = 0.0001;
    private final static double PHI = 0.9;
    private final static double P = 0.95;

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

        while (c < L){

            Solution aux = solution.copy();
            aux.swap();

            if(aux.getCost() < solution.getCost() + temperature){
                solution = aux;
                c++;
                r += solution.getCost();
            }
        }

        return new LoteResponse(r, solution);
    }

    public static void aceptacionPorUmbrales(double temperature, Solution solution){
        double p = 0.0;
        LoteResponse minSolution = null;
        int i = 0;

        String costo = String.format("%20.20f", (new BigDecimal(solution.getCost())));
        String temperatura = String.format("%20.20f", (new BigDecimal(temperature)));
        System.out.println(String.format("Lote: %4d      Cost: %40s      Temperature: %40s     Feasible: %b", 
        i, costo, temperatura, solution.getFeasible()));

        while (temperature > EPSILON){

            double q = Double.MAX_VALUE;
            
            while (p <= q ){
                q = p;
                minSolution = calculaLote(temperature, solution);
                i++;
                p  = minSolution.getPromedio();
                solution = minSolution.getSolution();
            }
            temperature = PHI * temperature;

            costo = String.format("%20.20f", (new BigDecimal(solution.getCost())));
            temperatura = String.format("%20.20f", (new BigDecimal(temperature)));
            System.out.println(String.format("Lote: %4d      Cost: %40s      Temperature: %40s     Feasible: %b", 
            i, costo, temperatura, solution.getFeasible()));
        }
        System.out.println("Solucion: " + minSolution.getSolution().getCost());
    }

    public static double temperaturaInicial(Solution solution, double T, double P){
        double p = porcentajeAceptados(solution, T);
        double T1, T2 = 0;


        if (Math.abs(P - p) < EPSILON){
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
        int N = 2000;

        for(int i = 0; i < N; i++){
            Solution aux = solution.copy();

            aux.swap();

            if(aux.getCost() < solution.getCost() + temperature){
                c++;
                solution = aux;
            }
        }
        return c/N;
    }

    public static double busquedaBinaria(Solution solution, double t1, double t2, double P){
        double t = (t1 + t2)/2;
        
        if (t2 - t1 < EPSILON){
            return t;
        }
        
        double p = porcentajeAceptados(solution, t);

        if (Math.abs(P - p) < EPSILON){
            return t;
        }
        if (p > P)
            return busquedaBinaria(solution, t1, t, P);
        else
            return busquedaBinaria(solution, t, t2, P);
    }

    public static void main(String[] args) {
        int[] idCitiesPath = {1,2,3,4,5,6,7,75,163,164,165,168,172,327,329,331,332,333,489,490,491,492,493,496,652,653,654,656,657,792,815,816,817,820,978,979,980,981,982,984};
    
        int[] permutacion = permutarArreglo(idCitiesPath);

        Solution tsp = new Solution(permutacion);

        double T = temperaturaInicial(tsp, TEMPERATURA_INICIAL, P);

        System.out.println("Temperatura inicial calculada: " + (new BigDecimal(T)).toPlainString());
        System.out.println("------------------------------ Inicia heuristica -----------------------");
        aceptacionPorUmbrales(T, tsp);

        System.out.println(tsp.toString());
    }
}