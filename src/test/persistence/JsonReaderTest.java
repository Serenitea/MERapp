package persistence;

import model.Pet;
import model.PetList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {
    private static final String JSON_STORE = "./test.json";
    private PetList testPetList = new PetList();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
/*
    private Pet testPet1;
    private Pet testPet2;
    private PetList testPetList0;
    private PetList testPetList1;
    private PetList testPetList2;

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
    }
*/


/*    public void checkPetList(String ownerName, ArrayList<Pet> myPets, PetList petList) {
        assertEquals(ownerName, testPetList0.getOwnerName());
        assertEquals(myPets, petList.getPetArray());
    }*/

    /*
    EFFECTS: Given the expected fields of a Pet and the Pet itself, check if the
    field values check expected values
     */
    public void checkPet(String petName, double weightInKg, double mer, double dietCalPerKg, Pet pet) {
        assertEquals(petName, pet.getPetName());
        assertEquals(weightInKg, pet.getWeight());
        assertEquals(mer, pet.getmer());
        assertEquals(dietCalPerKg, pet.getDietCalPerKg());
    }

    @BeforeEach
    public void setUp() {
        testPetList = new PetList();
    }

/*    @Test
    public void writeTest() {
        File myObj = new File("./data/filename.txt");
        System.out.println(myObj.getAbsolutePath());
        System.out.println(myObj.getPath());
        PathMatcher matcher =
                FileSystems.getDefault().getPathMatcher("glob:*.*");
        System.out.println(matcher);
    }*/

    @Test
    void testReaderNonExistentFile() {
        jsonReader = new JsonReader("./data/noSuchFile.json");
        try {
            testPetList = jsonReader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyPetList() {
        jsonReader = new JsonReader("./data/testReaderEmptyPetList.json");
        try {
            testPetList = jsonReader.read();
            assertEquals("", testPetList.getOwnerName());
            assertEquals(0, testPetList.getNumPets());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPetList() {
        jsonReader = new JsonReader("./data/testReaderGeneralPetList.json");
        try {
            testPetList = jsonReader.read();
            assertEquals("Sam", testPetList.getOwnerName());
            List<Pet> petList = testPetList.getPetArray();
            assertEquals(2, petList.size());
            checkPet("Loot", 29, 0, 0, petList.get(0));
            checkPet("burdock", 2, 0, 0, petList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}