package model;

import java.util.ArrayList;

public class PetList {
    private ArrayList<Pet> myPets;

    /*EFFECTS: constructs a PetList with an empty myPets ArrayList.*/
    public PetList() {
        myPets = new ArrayList<>();
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
        return myPets;
    }

    /*
    EFFECTS: returns number of Pet objects in the myPets ArrayList of a PetList.
    */
    public int getNumPets() {
        return myPets.size();
    }






}
