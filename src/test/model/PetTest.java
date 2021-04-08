package model;


/*import org.json.JSONObject;*/
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
/*    private JSONObject EXPECTED_RETURNED_PET;*/


    //initialize 2 pets for testing
    @BeforeEach
    public void setUp() {
        testPet1 = new Pet("Pretzl",
                19.5);
        testPet2 = new Pet("Peach",
                40, 450.3);
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
    public void testDietCalPerKg() {

        assertEquals(0, testPet1.getDietCalPerKg());
        testPet1.setNewDiet(42.0);
        assertEquals(42.0, testPet1.getDietCalPerKg());

        assertEquals(450.3, testPet2.getDietCalPerKg());
        testPet2.setNewDiet(4.0);
        assertEquals(4.0, testPet2.getDietCalPerKg());
    }

    @Test
    public void testMER() {

        assertEquals(0, testPet1.getMER());
        testPet1.setMER(42.0);
        assertEquals(42.0, testPet1.getMER());

        assertEquals(0, testPet2.getMER());
        testPet2.setMER(4.0);
        assertEquals(4.0, testPet2.getMER());
    }

    @Test
    public void testPortraitPic() {
        assertEquals("", testPet1.getPortraitPic());
        testPet1.setPortraitPic("./data/null.png");
        assertEquals("./data/null.png", testPet1.getPortraitPic() );
    }


    @Test
    public void testToJson() {
        assertEquals("{\"weight\":19.5," +
                "\"MER\":0," +
                "\"Diet Caloric Content (KCal/kg)\":0," +
                "\"pet name\":\"Pretzl\"}",
                testPet1.toJson().toString());
    }


}