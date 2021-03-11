package model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MERfactorTest {

    @Test
    void getAvgFactor() {
        assertEquals(1.6, MERfactor.ADULTNEUTERED.getAvgFactor());
        assertEquals(1.8, MERfactor.ADULTINTACT.getAvgFactor());
        assertEquals(1.3 * 100, (Math.round(MERfactor.INACTIVE.getAvgFactor() * 100)));
        assertEquals(1, MERfactor.WEIGHTLOSS.getAvgFactor());
        assertEquals(1.5, MERfactor.WEIGHTGAIN.getAvgFactor());
        assertEquals(3.5, MERfactor.ACTIVE.getAvgFactor());
        assertEquals(3, MERfactor.YOUNGPUPPY.getAvgFactor());
        assertEquals(2, MERfactor.OLDPUPPY.getAvgFactor());

    }

    @Test
    void getLowFactor() {
        assertEquals(1.2, MERfactor.INACTIVE.getLowFactor());
        assertEquals(1.2,MERfactor.WEIGHTGAIN.getLowFactor());
        assertEquals(2, MERfactor.ACTIVE.getLowFactor());
    }

    @Test
    void getHighFactor() {
        assertEquals(1.4, MERfactor.INACTIVE.getHighFactor());
        assertEquals(1.8,MERfactor.WEIGHTGAIN.getHighFactor());
        assertEquals(5, MERfactor.ACTIVE.getHighFactor());
    }

    @Test
    void values() {
    }

    @Test
    void valueOf() {
    }
}