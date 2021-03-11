package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.MERcalc.calcRER;
import static model.MERcalc.findMER;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MERcalcTest {
    private double testWeight;
    private double nullWeight;
    private static double TEMP_FACTOR = 1.6;

    @BeforeEach
    void setUp() {
        double testWeight = 10;
        double nullWeight = 0;
    }

    @Test
    double testCalcRER(double testWeight) {
        assertEquals(70*Math.pow(this.testWeight, 0.75), calcRER(this.testWeight));
        return testWeight;
    }

    @Test
    void testFindMER() {
        assertEquals(0,findMER(nullWeight, TEMP_FACTOR));
        assertEquals(70*Math.pow(this.testWeight, 0.75), findMER(testWeight, TEMP_FACTOR));
    }
}