package com.vrj.coh.tsp;

import lombok.Data;

@Data
public class LoteResponse {
    private double promedio;
    private Solution solution;

    public LoteResponse(double promedio, Solution tsp) {
        this.promedio = promedio;
        this.solution = tsp;
    }
}