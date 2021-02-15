package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
        testDiet1 = new Diet("Rayne Feline Kangaroo Canned", 1090);
        testDiet2 = new Diet("Hill's Canine t/d dry", 3504);
 */


//Unit tests for Pet
class PetTest {
    // test:
    private Pet pretzl;
    private Pet peach;
    private PetList petListTest;

    //initialize 2 pets of rtesting
    @BeforeEach
    public void setUp() {
        pretzl = new Pet("Pretzl",
                19.5);
        peach = new Pet("Peach",
                40);
        petListTest = new PetList();
    }

    @Test
    public void testNames() {
        assertEquals("Pretzl", pretzl.getPetName());
        assertEquals("Peach", peach.getPetName());
    }

    @Test
    public void testWeights() {
        assertEquals(19.5, pretzl.getWeight());
        assertEquals(40, peach.getWeight());
    }

    @Test
    public void setPetListTest() {
        petListTest.add(peach);
        assertEquals(1, petListTest.getPetArray().size());

        petListTest.add(pretzl);
        assertEquals(2, petListTest.getPetArray().size());
    }

    @Test
    public void addWeightTest() {
        pretzl.setWeight(15);
        assertEquals(15, pretzl.getWeight());

        peach.setWeight(17);
        assertEquals(17, peach.getWeight());
    }


}

