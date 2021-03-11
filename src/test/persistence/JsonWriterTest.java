package persistence;

import model.Pet;
import model.PetList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {
    private static final String JSON_STORE = "./test.json";
    private PetList testPetList = new PetList();
    private JsonWriter jsonWriter;

    @Test
    void testWriterInvalidFile() {
        try {
            jsonWriter = new JsonWriter("./data/my\0illegal:fileName.json");
            jsonWriter.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPetList() {
        jsonWriter = new JsonWriter("./data/testWriterEmptyPetList.json");

        try {
            jsonWriter.open();
            jsonWriter.write(testPetList);
            jsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPetList.json");
            testPetList = reader.read();
            assertEquals("", testPetList.getOwnerName());
            assertEquals(0, testPetList.getNumPets());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPetList() {
        jsonWriter = new JsonWriter("./data/testWriterGeneralPetList.json");
        Pet newTestPet1 = new Pet("Zoomer", 10.1);
        Pet newTestPet2 = new Pet("Trist", 15.3);
        testPetList.add(newTestPet1);
        testPetList.add(newTestPet2);
        testPetList.setOwnerName("Singer");
        try {
            jsonWriter.open();
            jsonWriter.write(testPetList);
            jsonWriter.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPetList.json");
            PetList readPetList = reader.read();
            assertEquals("Singer", readPetList.getOwnerName());
            List<Pet> readPets = readPetList.getPetArray();
            assertEquals(2, readPets.size());
            checkPet("Zoomer", 10.1, 0, 0, readPets.get(0));
            checkPet("Trist", 15.3, 0, 0, readPets.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}