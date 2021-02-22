package model;

import java.util.ArrayList;

public class PetList {
    private ArrayList<Pet> myPets;

    /*EFFECTS: constructs a PetList with an empty myPets ArrayList.*/
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

    /*MODIFIES: this
    EFFECTS: removes an inputted Pet to the myPets ArrayList.*/
    public void remove(Pet petToRemove) {
        myPets.remove(petToRemove);
    }

    /*
     EFFECTS: returns true if the string input matches the name of one of the Pet objects in PetList
     */
    public boolean duplicateName(String checkName) {
        boolean duplicate = false;
        for (int i = 0; i < myPets.size(); i++) {
            if (checkName.equalsIgnoreCase(myPets.get(i).getPetName())) {
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

    /*
    EFFECTS: prints all pets with all of their fields to the user console.
    TODO add test
     */
    public void printAllPets() {
        for (int i = 0; i < myPets.size(); i++) {
            //System.out.println(myPets.get(i).getPetName());
            myPets.get(i).printPetAttributes();
            System.out.println("\n---------------------------");
        }
    }

    /*
    EFFECTS: prints each pet's name to the user console.
     */
    public void printAllNames() {
        for (int i = 0; i < myPets.size(); i++) {
            System.out.println(myPets.get(i).getPetName());
        }
    }


}
