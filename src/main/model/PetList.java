package model;

import org.json.JSONArray;
import org.json.JSONObject;
/*import persistence.Writable;*/

import java.util.ArrayList;

/*
Represents a user profile with a number of pets
 */
public class PetList /*implements Writable*/ {
    private ArrayList<Pet> myPets;
    private String ownerName;


    /*EFFECTS: constructs a PetList with an empty myPets ArrayList.*/
    public PetList() {
        this.myPets = new ArrayList<>();
    }

    /*
    MODIFIES: this
    EFFECTS: adds an inputted Pet to the myPets ArrayList.
     */
    public void add(Pet petToAdd) {
        myPets.add(petToAdd);
    }

    /*MODIFIES: this
    EFFECTS: removes an inputted Pet to the myPets ArrayList.*/
    public void remove(Pet petToRemove) {
        myPets.remove(petToRemove);
    }

    /*
     EFFECTS: returns true if the string input matches the name of at least one Pet's name in PetList
     */
    public boolean duplicateName(String checkName) {
        boolean duplicate = false;
        for (Pet myPet : myPets) {
            if (checkName.equalsIgnoreCase(myPet.getPetName())) {
                duplicate = true;
                break;
            }
        }
        return duplicate;
    }

    /*
    GETTER
     */
    public ArrayList<Pet> getPetArray() {
        return this.myPets;
    }

    /*
    EFFECTS: returns number of Pet objects in the myPets ArrayList of a PetList.
    */
    public int getNumPets() {
        return this.myPets.size();
    }

    /*
    EFFECTS: returns owner name field
     */
    public String getOwnerName() {
        return this.ownerName;
    }

    /*
    MODIFIES: this
    EFFECTS: sets the ownerName field to the inputted String
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ownername", this.ownerName);
        json.put("pets", petsToJson());
        return json;
    }

    private JSONArray petsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Pet pet : this.myPets) {
            jsonArray.put(pet.toJson());
        }
        return jsonArray;
    }
}
