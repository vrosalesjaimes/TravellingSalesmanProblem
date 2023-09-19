package com.vrj.coh.tsp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cost {
    private BigDecimal sum;
    private double normalizer;

    public BigDecimal getCost(){
        return this.sum.divide(new BigDecimal(this.normalizer), RoundingMode.HALF_UP);
    }

    public void sum(BigDecimal suma){
        this.sum = this.sum.add(suma);
    }
}
