package model;

//import model.MERfactor;
//Currently not integrated into main app. temporarily static factor TEMP_FACTOR of 1.6, to be replaced by MERfactor
//Class representing a MER calculation for determining nutritional requirements of a pet.
public class MERcalc {
    private double factor;
    private static final double TEMP_FACTOR = 1.6;

    public MERcalc() {
    }

    /*
    EFFECTS: returns a pet's RER given weight in kg. RER = 70*(wt^0.75)
     */
    private static double calcRER(double weight) {
        double powCalc = Math.pow(weight, 0.75);
        return 70 * powCalc;
    }

    /*
    EFFECTS: returns a pet's MER given the weight in kg and a nutritional factor (usually between 0.6 to 5)
     */
    public static double calcMER(double weight, double factor) {
        double rer = calcRER(weight);
        return factor * rer;
    }

    /*
    overloaded method
    EFFECTS: returns a pet's MER given the weight in kg and the default nutritional factor 1.6
     */
    public static double calcMER(double weight) {
        double rer = calcRER(weight);
        return 1.6 * rer;
    }

    /*
    MODIFIES: this
    EFFECTS: sets factor of the MER calculation to the inputted value.
     */
    public void setFactor(double newFactor) {
        this.factor = newFactor;
    }

    /*
    EFFECTS: returns factor of initialized MER calculation
     */
    public double getFactor() {
        return this.factor;
    }

}
