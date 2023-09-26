package com.vrj.coh.tsp;

import lombok.Data;

@Data
public class LotResponse {
    private double promedio;
    private Solution solution;

    public LotResponse(double promedio, Solution tsp) {
        this.promedio = promedio;
        this.solution = tsp;
    }
}