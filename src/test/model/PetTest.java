package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    private Pet testPet1;
    private Pet testPet2;
    private PetList testPetList;


    //initialize 2 pets for testing
    @BeforeEach
    public void setUp() {
        testPet1 = new Pet("Pretzl",
                19.5);
        testPet2 = new Pet("Peach",
                40);
    }

    @Test
    public void testNames() {
        assertEquals("Pretzl", testPet1.getPetName());
        testPet1.setNewName("Poof");
        assertEquals("Poof", testPet1.getPetName());

        assertEquals("Peach", testPet2.getPetName());
        testPet2.setNewName("Pink");
        assertEquals("Pink", testPet2.getPetName());
    }

    @Test
    public void testWeights() {
        assertEquals(19.5, testPet1.getWeight());
        assertEquals(40, testPet2.getWeight());
    }

    @Test
    public void addWeightTest() {
        testPet1.setWeight(15.0);
        assertEquals(15.0, testPet1.getWeight());

        testPet2.setWeight(17.4);
        assertEquals(17.4, testPet2.getWeight());
    }

    @Test
    public void testChangeName() {
        testPet1.setNewName("Pretzel");
        assertEquals("Pretzel", testPet1.getPetName());
        testPet2.setNewName("Peaches");
        assertEquals("Peaches", testPet2.getPetName());
    }


}