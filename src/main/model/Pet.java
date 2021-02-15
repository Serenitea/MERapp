package model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;

// Represents a cat or dog having a name and stored information relating to its energy requirements:
// species, gender, neuter status, age in years, weight (kg), resting energy requirements, energy multiplicative factor,
// BCS (body condition score)
public class Pet {
    private String petName;
    private double weightInKg;

    /*
     * REQUIRES:
     * - petName has a non-zero length, no existing pet with identical name
     * - body weight in kg > 0
     */

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
    MODIFIES: this.weightInKg
    EFFECTS: change's the weight (kg) of a pet.
     */
    public void setWeight(double newWeight) {
        this.weightInKg = newWeight;
    }

    /*
MODIFIES: this.petName
EFFECTS: changes a pet's name to the inputted String argument.
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

    /*
     * MODIFIES: this
     * EFFECTS: changes the current diet of the pet to the newly inputted one.
     */
//    public void addDiet(Diet newDiet) {
//        this.currentDiet = newDiet;
//    }

}

