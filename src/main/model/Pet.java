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
    public String petName;
    private String species;
    private String gender;
    private boolean neuterStatus;
    public double weightInKg;
    private double restingEnergyRequirements;
    private double energyFactor;
    private int bodyConditionScore;
    private int dietKCalPerKg;
    private HashMap weightHx;
    private double recDailyFeedingGrams;

    /*
     * REQUIRES:
     * - petName has a non-zero length, no existing pet with identical name
     * - species is dog/cat,
     * - gender is M/F,
     * - neuterStatus is boolean
     * - ageInYears as an integer
     * - body weight in kg,
     * - Body Condition Score scored from 1-9
     * - energyFactor of pet as determined by lifestyle
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

        this.species = species;
        this.gender = gender;
        this.neuterStatus = neuterStatus;
        this.weightInKg = weightInKg;
        this.bodyConditionScore = bodyConditionScore;
        this.energyFactor = energyFactor;
        this.restingEnergyRequirements = this.energyFactor * 70 * Math.pow(this.weightInKg, 0.75);
        this.dietKCalPerKg = dietKCalPerKg;
        this.recDailyFeedingGrams = this.restingEnergyRequirements / dietKCalPerKg * 1000;
//        this.weightHx = new HashMap();
    }

    /*
    MODIFIES: this
    EFFECTS: adds a weight (kg) and date of weight to a pet's profile.
     */
    public void addWeight(double newWeight, Date dateOfWeight) {
        this.weightInKg = newWeight;
        this.weightHx.put(newWeight, dateOfWeight);
    }

    //
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

