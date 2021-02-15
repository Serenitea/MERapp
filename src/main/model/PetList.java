package model;

import java.util.ArrayList;

public class PetList {
    private ArrayList<Pet> myPets;

    public PetList() {
        myPets = new ArrayList<Pet>();
    }

    /*
    MODIFIES: this
    EFFECTS: adds an inputted Pet to the myPets ArrayList.
     */
    public void add(Pet petToAdd) {
        myPets.add(petToAdd);
    }

    /*
    GETTER
     */
    public ArrayList<Pet> getPetArray() {
        return myPets;
    }

    /*
    EFFECTS: prints each pet's name to the user console.
     */
    public void printPets() {
        for (int i = 0; i < myPets.size(); i++) {
            System.out.println(myPets.get(i).getPetName());
        }
    }



}
