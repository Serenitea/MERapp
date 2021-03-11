package model;

//standard ranges of nutritional factors associated with each lifestyle, used in calculating MER
//currently unintegrated into main app and only applies to dogs.
@SuppressWarnings("SpellCheckingInspection")
public enum MERfactor {
    //enumeration of lifestyles and their associated min/max factor range
    ADULTNEUTERED(1.6, 1.6),
    ADULTINTACT(1.8, 1.8),
    INACTIVE(1.2, 1.4),
    WEIGHTLOSS(1.0, 1.0),
    WEIGHTGAIN(1.2, 1.8),
    ACTIVE(2, 5),
    YOUNGPUPPY(3, 3),
    OLDPUPPY(2, 2);


    private final double lowFactor;
    private final double highFactor;

    //organizes each lifestyle into minFactor and maxFactor
    MERfactor(double minFactor, double maxFactor) {
        this.lowFactor = minFactor;
        this.highFactor = maxFactor;
    }

    /*
    REQUIRES: assumes pet species is dog.
    EFFECTS: returns the average of the factors range of a lifestyle.
     */
    public double getAvgFactor() {
        return ((highFactor + lowFactor) / 2);
    }

    /*
    REQUIRES: assumes pet species is dog.
    EFFECTS: returns the low end of a factors range of a lifestyle.
     */
    public double getLowFactor() {
        return lowFactor;
    }

    /*
    REQUIRES: assumes pet species is dog.
    EFFECTS: returns the high end of a factors range of a lifestyle.
     */
    public double getHighFactor() {
        return highFactor;
    }


}
