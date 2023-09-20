package com.vrj.coh.tsp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cost {
    private Double sum;
    private double normalizer;

    public Double getCost(){
        return this.sum/ this.normalizer;
    }

    public void sum(Double suma){
        this.sum = this.sum + suma;
    }
}
