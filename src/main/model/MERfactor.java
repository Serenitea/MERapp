package model;

//standard ranges of nutritional factors used in calculating MER
//currently unintegrated into main app and only applies to dogs.
@SuppressWarnings("SpellCheckingInspection")
public enum MERfactor {
    ADULTNEUTERED(1.6, 1.6),
    ADULTINTACT(1.8, 1.8),
    INACTIVE(1.2, 1.4),
    WEIGHTLOSS(1.0,1.0),
    WEIGHTGAIN(1.2, 1.8),
    ACTIVE(2, 5),
    YOUNGPUPPY(3, 3),
    OLDPUPPY(2, 2);


    private final double lowFactor;
    private final double highFactor;


    MERfactor(double minFactor, double maxFactor) {
        this.lowFactor = minFactor;
        this.highFactor = maxFactor;
    }

    public double getAvgFactor() {
        return ((highFactor + lowFactor) / 2);
    }

    public double getLowFactor() {
        return lowFactor;
    }

    public double getHighFactor() {
        return highFactor;
    }



}
