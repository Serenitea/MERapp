package model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.util.Iterator;

// Represents a cat or dog having a name and stored information relating to its energy requirements:
// species, gender, neuter status, age in years, weight (kg), resting energy requirements, energy multiplicative factor,
// BCS (body condition score)
public class Pet {
    protected String petName;
    protected double weightInKg;

    /*
     REQUIRES:
     - petName has a non-zero length, no existing pet with identical name
     - body weight in kg > 0
     */
    //TODO add dietCal

    public Pet(String petName,
               double weightInKg
            /* String species,
               String gender,
               boolean neuterStatus,
               ,
               int bodyConditionScore,
               double energyFactor,
               int dietKCalPerKg*/
    ) {
        this.petName = petName;
        this.weightInKg = weightInKg;

    }

    /*
    MODIFIES: this
    EFFECTS: changes a pet's name (the petName field) to the inputted String argument.
    */
    public void setNewName(String newName) {
        this.petName = newName;
    }

    //GETTERS
    public double getWeight() {
        return this.weightInKg;
    }

    public String getPetName() {
        return this.petName;
    }

    //TODO add fn for outputting attributes of a pet

    /*
    EFFECTS: Prints all fields for a Pet.
    */
    public void printPetAttributes() {
        System.out.println("\nName: " + this.petName);
        System.out.println("\nWeight: " + this.getWeight());
    }

    /*
    MODIFIES: this.weightInKg
    EFFECTS: change's the weight (kg) of a pet.
     */
    public void setWeight(double newWeight) {
        this.weightInKg = newWeight;
    }



    //TODO add fn for calculating MER from weight



    /*
     MODIFIES: this
     EFFECTS: changes the current diet of the pet to the newly inputted one.
     */
//    public void addDiet(Diet newDiet) {
//        this.currentDiet = newDiet;
//    }

}

