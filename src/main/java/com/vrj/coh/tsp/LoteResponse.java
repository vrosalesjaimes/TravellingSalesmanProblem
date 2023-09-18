package com.vrj.coh.tsp;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LoteResponse {
    private BigDecimal promedio;
    private Solution solution;

    public LoteResponse(BigDecimal promedio, Solution tsp) {
        this.promedio = promedio;
        this.solution = tsp;
    }
}