package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.MERcalc.calcMER;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MERcalcTest {
    private double testWeight;
    private double nullWeight;
    private static final double TEMP_FACTOR = 1.6;
    private static final double TEST_FACTOR = 1.7;
    private final MERcalc testMERcalc = new MERcalc();

    @BeforeEach
    void setUp() {
        double testWeight = 10;
        double nullWeight = 0;
    }


    @Test
    void testFindMER() {
        assertEquals(0, calcMER(nullWeight, TEMP_FACTOR));
        assertEquals(70*Math.pow(this.testWeight, 0.75), calcMER(testWeight, TEMP_FACTOR));
    }

    @Test
    void testSetFactor() {
        testMERcalc.setFactor(1.7);
        assertEquals(1.7, testMERcalc.getFactor());
    }

}