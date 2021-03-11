package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetListTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // test:
    private Pet testPet1;
    private Pet testPet2;
    private PetList testPetList0;
    private PetList testPetList1;
    private PetList testPetList2;

    //initialize 2 pets of testing
    @BeforeEach
    public void setUp() {
        testPet1 = new Pet("Pretzl",
                19.5);
        testPet2 = new Pet("Peach",
                40);
        testPetList0 = new PetList();
        testPetList1 = new PetList();
        testPetList1.add(testPet1);
        testPetList2 = new PetList();
        testPetList2.add(testPet1);
        testPetList2.add(testPet2);
        System.setOut(new PrintStream(outContent));
    }


    @Test
    public void testDuplicateName() {
        assertTrue(testPetList1.duplicateName("pretzl"));
        assertFalse(testPetList1.duplicateName("peaches"));
    }

    @Test
    public void testOwnerName() {
        assertEquals("",testPetList1.getOwnerName());
        testPetList1.setOwnerName("Jason");
        assertEquals("Jason",testPetList1.getOwnerName());
    }


    @Test
    //tests adding, removing, printing, getting size from PetList
    public void testAddAndRemovePet() {
        testPetList0.add(testPet1);
        assertEquals(1, testPetList0.getPetArray().size());
        assertEquals(1, testPetList0.getNumPets());

        testPetList0.add(testPet2);
        assertEquals(2, testPetList0.getPetArray().size());
        assertEquals(2, testPetList0.getNumPets());

        testPetList0.remove(testPet1);
        assertEquals(1, testPetList0.getPetArray().size());
        assertEquals(1, testPetList0.getNumPets());
    }

    @Test
    public void testToJson() {
        testPetList1.setOwnerName("Jason");
        assertEquals("{\"pets\":[{\"weight\":19.5," +
                "\"MER\":0," +
                "\"Diet Caloric Content (KCal/kg)\":0," +
                "\"pet name\":\"Pretzl\"}]," +
                "\"ownername\":\"Jason\"}",testPetList1.toJson().toString());
    }

}