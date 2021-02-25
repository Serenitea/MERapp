package model;

//import model.MERfactor;

public class MERcalc {
    public static double calcRER(double weight) {
        return Math.pow(70 * weight, 0.75);
    }

    public double estFactor() {
        return 1.6; //placeholder static var until later
    }

    public static double findMER(double weight) {
        double factor = 1.6;
        double rer = calcRER(weight);
        return factor * rer;
    }
}
