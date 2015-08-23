package ga;

/**
 * Created by drufener on 8/22/15.
 */
public class MajorityGoal implements Goal {

    private static final int DURATION = 100;

    @Override
    public  boolean satisfactory(Individual individual) {
        if (majorityBlack(individual.getInitialCondition())) {
            for (boolean cell : individual.getCurrentState()) {
                if (!cell) {
                    return false;
                }
            }
        } else {
            for (boolean cell : individual.getCurrentState()) {
                if (cell) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    private boolean majorityBlack(boolean[] initialCondition) {
        int numBlack = 0;

        for (boolean cell : initialCondition) {
            if (cell) {
                numBlack++;
            }
        }

        return numBlack > 0.5 * initialCondition.length;

    }
}
