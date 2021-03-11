package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a cat or dog having a name and stored information relating to its energy requirements:
//  weight (kg), resting energy requirements, energy multiplicative factor,
// BCS (body condition score)
public class Pet implements Writable {
    private String petName;
    private double weightInKg;
    private double mer;
    private double dietCalPerKg;

    /*
     Initializes Pet object with user inputted fields for name and weight.
     New Pets are initialized with 0 mer and 0 dietCalPerKg.
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
        //this.mer = MERcalc.findMER(weightInKg);
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

    public void setWeight(
            double newWeight) {
        this.weightInKg = newWeight;
    }

    public double getDietCalPerKg() {
        return this.dietCalPerKg;
    }

    /*
    MODIFIES: this
    EFFECTS: changes the weight (kg) of a pet.
     */

    public double getMER() {
        return this.mer;
    }
    /*
    MODIFIES: this
    EFFECTS: changes the MER of a pet.
     */
    public void setMER(double newMER) {
        this.mer = newMER;
    }

    /*
    MODIFIES: this
    EFFECTS: changes a pet's name (the petName field) to the inputted String argument.
    */
    public void setNewName(String newName) {
        this.petName = newName;
    }

    /*
    MODIFIES: this
    EFFECTS: changes a pet's diet (the dietCalPerKg field) to the inputted number argument.
    */
    public void setNewDiet(double newPetDiet) {
        this.dietCalPerKg = newPetDiet;
    }

    /*
    EFFECTS: returns the Json string serialization of a Pet
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pet name", petName);
        json.put("weight", weightInKg);
        json.put("MER", mer);
        json.put("Diet Caloric Content (KCal/kg)", dietCalPerKg);
        return json;
    }

}

