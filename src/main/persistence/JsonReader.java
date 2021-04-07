package persistence;

/*import model.Category;
import model.pet;
import model.PetList;*/

import model.Pet;
import model.PetList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads PetList from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads PetList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PetList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePetList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses PetList from JSON object and returns it
    private PetList parsePetList(JSONObject jsonObject) throws JSONException {
        PetList petList = new PetList();
        String name = jsonObject.getString("ownername");
        petList.setOwnerName(name);
        addPets(petList, jsonObject);
        return petList;
    }

    // MODIFIES: petList
    // EFFECTS: parses Pets from JSON object and adds them to PetList
    private void addPets(PetList petList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("pets");
        for (Object json : jsonArray) {
            JSONObject nextPet = (JSONObject) json;
            addPet(petList, nextPet);
        }
    }

    // MODIFIES: petList
    // EFFECTS: parses pet from JSON object and adds it to PetList
    private void addPet(PetList petList, JSONObject jsonObject) {
        String name = jsonObject.getString("pet name");
        double weight = jsonObject.getDouble("weight");
        Pet pet = new Pet(name, weight);
        /*try {
            double mer = jsonObject.getDouble("MER");
        } catch (NullPointerException e) {
        }*/
        petList.add(pet);
    }
}
