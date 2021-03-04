package model;

import org.json.JSONObject;
/*import persistence.Writable;*/

// Represents a cat or dog having a name and stored information relating to its energy requirements:
// species, gender, neuter status, age in years, weight (kg), resting energy requirements, energy multiplicative factor,
// BCS (body condition score)
public class Pet /*implements Writable*/ {
    private String petName;
    private double weightInKg;
    private double mer;
    private double dietCalPerKg;

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
        //this.mer = MERcalc.findMER(weightInKg); todo
        this.mer = 0;
        this.dietCalPerKg = 0;
    }

    //GETTERS
    public String getPetName() {
        return this.petName;
    }

    public double getWeight() {
        return this.weightInKg;
    }

    public double getDietCalPerKg() {
        return this.dietCalPerKg;
    }

    public double getmer() {
        return this.mer;
    }

    /*
            MODIFIES: this.weightInKg
            EFFECTS: change's the weight (kg) of a pet.
             */
    public void setWeight(
            double newWeight) {
        this.weightInKg = newWeight;
    }

    public double getMer() {
        return mer;
    }

    /*
    MODIFIES: this
    EFFECTS: changes a pet's name (the petName field) to the inputted String argument.
    */
    public void setNewName(String newName) {
        this.petName = newName;
    }

    public void setNewDiet(double newPetDiet) {
        this.dietCalPerKg = newPetDiet;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pet name", petName);
        json.put("weight", weightInKg);
        json.put("MER", mer);
        json.put("Diet Caloric Content (KCal/kg)", dietCalPerKg);
        return json;
    }

    /*
     MODIFIES: this
     EFFECTS: changes the current diet of the pet to the newly inputted one.
     */
//    public void addDiet(Diet newDiet) {
//        this.currentDiet = newDiet;
//    }

}

