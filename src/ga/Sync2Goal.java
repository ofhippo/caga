package ga;

import java.util.List;

/**
 * Created by drufener on 8/22/15.
 */
public class Sync2Goal implements Goal {
    @Override
    public boolean satisfactory(Individual individual) {
        List<boolean[]> history = individual.getStateHistory();

        boolean[] stateA = history.get(history.size() - 3);
        boolean[] stateB = history.get(history.size() - 2);
        boolean[] stateC = history.get(history.size() - 1);
        return (allWhite(stateA) && allBlack(stateB) && allWhite(stateC)) || (allBlack(stateA) && allWhite(stateB) && allBlack(stateC));
    }

    @Override
    public int getDuration() {
        return 100;
    }

    private boolean allWhite(boolean[] state) {
        for (boolean cell : state) {
            if (cell) {
                return false;
            }
        }
        return true;
    }

    private boolean allBlack(boolean[] state) {
        for (boolean cell : state) {
            if (!cell) {
                return false;
            }
        }
        return true;
    }
}
