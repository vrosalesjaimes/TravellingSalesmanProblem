package com.vrj.coh.tsp.model;

import lombok.Data;

import java.util.Arrays;

@Data
public class Solution {
    /* Sum of the k-1 edges assuming that k is the number of cities. */
    private double normalizer;
    /* Maximum distance between two adjacent cities. */
    private double maximum;
    /* Sum of the distances between the cities. */
    private double evaluation;
    /* Array of cities that represents the path. */
    private Integer[] path;
    /* Boolean that indicates if the solution is feasible or not. */
    private boolean isFeasible;

    /* Initializes an object of type solution. */
    public Solution(double normalizer, double maximum, double evaluation, Integer[] path, boolean isFeasible){
        this.normalizer = normalizer;
        this.maximum = maximum;
        this.evaluation = evaluation;
        this.path = path;
        this.isFeasible = isFeasible;
    }

    /**
     * Returns the string representation of a solution.
     */
    @Override
    public String toString(){
        String s = "Path: " + Arrays.toString(this.path) +
                    "\n Maximum: " + this.maximum +
                    "\n Normalizer: " +  this.normalizer +
                    "\n Evaluation: " + this.evaluation +
                    "\n Feasible: " + this.isFeasible + "\n";
        return s;
    }
}
