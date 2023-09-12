package com.vrj.coh.tsp.service;

import com.vrj.coh.tsp.Tsp;
import org.springframework.stereotype.Service;

@Service
public class ThresholdAcceptingService {

    public Object[] calculaLote(double temperature, Tsp tsp){
        int c = 0;
        double r = 0.0;
        int L = 2000;
        while (c < L){
            Tsp aux = tsp.copy();
            aux.swap();

            if(aux.getCost() < tsp.getCost()){
                tsp = aux;
                c++;
                r += tsp.getCost();
            }
        }

        Object[] result = {tsp, r/L};

        return result;
    }

    public void aceptacionPorUmbrales(double temperature, Tsp tsp, double epsilon, double phi){
        double p = 0.0;

        while (temperature > epsilon){
            double q = Double.MAX_VALUE;
            
            while (p <= q ){
                q = p;
                Object[] lot = calculaLote(temperature, tsp);
            }

            temperature = phi * temperature;
        }
    }

    public double temperaturaInicial(Tsp tsp, double T, double P, double epsilonP){
        double p = porcentajeAceptados(tsp, T);
        double T1, T2 = 0;


        if (Math.abs(P - p) < epsilonP){
            return T;
        }

        if (p < P){
            while (p < P){
                T = 2 * T;
                p = porcentajeAceptados(tsp, T);
            }
            T1 = T/2;
            T2 = T;
        } else {
            while (p > P){
                T = T/2;
                p = porcentajeAceptados(tsp, T);
            }
            T1 = T;
            T2 = 2 * T;
        }

        return busquedaBinaria(tsp, T1, T2, P, epsilonP);
    }

    public double porcentajeAceptados(Tsp tsp, double temperature){
        int c = 0;
        int N = 1000;

        for(int i = 0; i < N; i++){
            Tsp aux = tsp.copy();
            aux.swap();

            if(aux.getCost() < tsp.getCost()){
                c++;
                tsp = aux;
            }
        }
        return c/N;
    }

    public double busquedaBinaria(Tsp tsp, double t1, double t2, double P, double epsilonP){
        double t = (t1 + t2)/2;
        
        if (t2 - t1 < epsilonP){
            return t;
        }
        
        double p = porcentajeAceptados(tsp, t);

        if (Math.abs(P - p) < epsilonP){
            return t;
        }
        if (p > P)
            return busquedaBinaria(tsp, t1, t, P, epsilonP);
        else
            return busquedaBinaria(tsp, t, t2, P, epsilonP);
    }
}