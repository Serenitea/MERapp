package model;

//import model.MERfactor;
//Currently not integrated into main app.
//Class of calculation functions for determining nutritional requirements of a pet.
public class MERcalc {
    private double factor;
    private static double TEMP_FACTOR = 1.6;

    /*
    EFFECTS: returns a pet's RER given weight in kg. RER = 70*(wt^0.75)
     */
    public static double calcRER(double weight) {
        double powCalc = Math.pow(weight, 0.75);
        return 70 * powCalc;
    }

    /*
    EFFECTS: returns a pet's MER given the weight in kg and a nutritional factor (usually between 0.6 to 5)
     */
    public static double findMER(double weight, double factor) {
        double rer = calcRER(weight);
        return factor * rer;
    }

/*    public double estFactor() {
        return 1.6; //placeholder static var until later
    }*/
}
