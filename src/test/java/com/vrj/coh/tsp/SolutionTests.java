package com.vrj.coh.tsp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolutionTests {

    private Random random;
    private Solution solution;

    @Before
    public void setUp() {
        random = new Random();
        double normalizer = generateRandomDouble(1.0, 100.0);
        double maximum = generateRandomDouble(1.0, 1000.0);
        double evaluation = generateRandomDouble(1.0, 500.0);
        Integer[] path = generateRandomIntArray(5, 1, 10000);
        boolean isFeasible = random.nextBoolean();
        solution = new Solution(normalizer, maximum, evaluation, path, isFeasible);
    }

    private double generateRandomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private Integer[] generateRandomIntArray(int size, int min, int max) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(solution.getNormalizer(), solution.getNormalizer(), 0.0);
        assertEquals(solution.getMaximum(), solution.getMaximum(), 0.0);
        assertEquals(solution.getEvaluation(), solution.getEvaluation(), 0.0);
        assertArrayEquals(solution.getPath(), solution.getPath());
        assertEquals(solution.isFeasible(), solution.isFeasible());
    }

    @Test
    public void testToString() {
        String expected = "Path: " + Arrays.toString(solution.getPath()) + "\n Maximum: " + solution.getMaximum() + "\n Normalizer: " + solution.getNormalizer() + "\n Evaluation: " + solution.getEvaluation() + "\n Feasible: " + solution.isFeasible() + "\n";
        assertEquals(expected, solution.toString());
    }

    @Test
    public void testNormalizerGetterAndSetter() {
        double normalizer = generateRandomDouble(1.0, 100.0);
        solution.setNormalizer(normalizer);
        assertEquals(normalizer, solution.getNormalizer(), 0.0);
    }

    @Test
    public void testMaximumGetterAndSetter() {
        double maximum = generateRandomDouble(1.0, 1000.0);
        solution.setMaximum(maximum);
        assertEquals(maximum, solution.getMaximum(), 0.0);
    }

    @Test
    public void testEvaluationGetterAndSetter() {
        double evaluation = generateRandomDouble(1.0, 500.0);
        solution.setEvaluation(evaluation);
        assertEquals(evaluation, solution.getEvaluation(), 0.0);
    }

    @Test
    public void testPathGetterAndSetter() {
        Integer[] path = generateRandomIntArray(5, 1, 10);
        solution.setPath(path);
        assertArrayEquals(path, solution.getPath());
    }

    @Test
    public void testIsFeasibleGetterAndSetter() {
        boolean isFeasible = random.nextBoolean();
        solution.setFeasible(isFeasible);
        assertEquals(isFeasible, solution.isFeasible());
    }
}
