package com.vrj.coh.tsp;

import lombok.Data;

@Data
public class LoteResponse {
    private double promedio;
    private Tsp tsp;

    public LoteResponse(double promedio, Tsp tsp) {
        this.promedio = promedio;
        this.tsp = tsp;
    }
}
