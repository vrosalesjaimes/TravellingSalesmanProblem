package com.vrj.coh.tsp;

<<<<<<< HEAD
=======
import java.math.BigDecimal;

>>>>>>> test
import lombok.Data;

@Data
public class LoteResponse {
<<<<<<< HEAD
    private double promedio;
    private Tsp tsp;

    public LoteResponse(double promedio, Tsp tsp) {
        this.promedio = promedio;
        this.tsp = tsp;
    }
}
=======
    private BigDecimal promedio;
    private Solution solution;

    public LoteResponse(BigDecimal promedio, Solution tsp) {
        this.promedio = promedio;
        this.solution = tsp;
    }
}
>>>>>>> test
