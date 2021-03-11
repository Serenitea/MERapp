package persistence;

import model.Pet;
import model.PetList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    private static final String JSON_STORE = "./test.json";
    private PetList testPetList = new PetList();
    private JsonReader jsonReader;

    public void checkPet(String petName, double weightInKg, double mer, double dietCalPerKg, Pet pet) {
        assertEquals(petName, pet.getPetName());
        assertEquals(weightInKg, pet.getWeight());
        assertEquals(mer, pet.getMER());
        assertEquals(dietCalPerKg, pet.getDietCalPerKg());
    }

}